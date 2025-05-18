include "../gateway/bankGatewayI.iol"
include "BankAccountsI.iol"

include "console.iol"
include "string_utils.iol"
include "json_utils.iol"
include "database.iol"
include "../locations.ol"

execution{ concurrent }

inputPort BankAccountsPort {
    Location: LOCATIONS_API_ACCOUNTS
    Protocol: xmlrpc { 
        .compression = false
        .debug = false
    }
    Interfaces: BankAccountsI
}

init {
    // connect to DB

    with ( connectionInfo ) {
        .username = "romanellas";
        .password = "59741404";
        .host = MYSQL_HOST;
        .database = "jolie_bank";
        .driver = "mysql"
    };
    connect@Database( connectionInfo )( void )
    println@Console("Bank Accounts is running...")()
}

//Requires: owner, account_id
define createAccount
{
    scope ( createAccountScope ) {
        sql_cmd = "SELECT CREATE_ACCOUNT(:owner) AS identifier";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("owner:" + owner)();
            println@Console(createAccountScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.owner = owner;
        query@Database( queryRequest )( queryReturn );
        if(#queryReturn.row > 0){
            if(queryReturn.row[0].identifier != 0){
                account_id = queryReturn.row[0].identifier
            }
        }
    }
}

//account, amount, check_result
define checkMoney 
{
    scope ( checkMoneyScope ) {
        sql_cmd = "SELECT COUNT(*) AS cnt FROM view_balances WHERE account = :account AND balance >= :amount";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("account:" + account)();
            println@Console("amount:" + amount)();
            println@Console(checkMoneyScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.account = account;
        queryRequest.amount = amount;
        query@Database( queryRequest )( queryReturn );
        if(#queryReturn.row > 0){
            if(queryReturn.row[0].cnt != 0){
                check_result = queryReturn.row[0].cnt
            }
        }
    }
}

//account, check_result
define checkAccount 
{
    scope ( checkMoneyScope ) {
        sql_cmd = "SELECT COUNT(*) AS cnt FROM tbl_account WHERE id = :account";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("account:" + account)();
            println@Console(checkMoneyScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.account = account;
        query@Database( queryRequest )( queryReturn );
        if(#queryReturn.row > 0){
            if(queryReturn.row[0].cnt != 0){
                check_result = queryReturn.row[0].cnt
            }
        }
    }
}

//account, amount
define depositMoney 
{
    scope ( depositMoneyScope ) {
        sql_cmd = "INSERT INTO tbl_deposit (account, deposit_value, deposit_on) VALUES (:account, :amount, CURRENT_TIMESTAMP())";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("account:" + account)();
            println@Console("amount:" + amount)();
            println@Console(depositMoneyScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.account = account;
        queryRequest.amount = amount;
        update@Database( queryRequest )( queryReturn )
    }
}


//pageSize, offset
define getAccounts 
{
    scope ( getAccountsScope ) {
        sql_cmd = "SELECT tbl_account.id, tbl_account.owner, view_balances.balance FROM tbl_account INNER JOIN view_balances ON (tbl_account.id = view_balances.account) LIMIT :offset, :pageSize";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("pageSize:" + pageSize)();
            println@Console("offset:" + offset)();
            println@Console(getAccountsScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.pageSize = pageSize;
        queryRequest.offset = offset;
        query@Database( queryRequest )( queryReturn );
        
        for ( i = 0, i < #queryReturn.row, i++ ) {
            output[i].account_id = queryReturn.row[i].id;
            output[i].owner = queryReturn.row[i].owner;
            output[i].balance = queryReturn.row[i].balance
        }
    }
}


main {
    [ postAccount( request )( response ) {
        account_id = -1;
        owner = request.param.owner;
        createAccount;
        response.param.identifier = account_id
    }]

    [ putWithdraw( request )( response ) {
        account = request.param.account;
        amount = request.param.amount;
        if(amount < 0) {
            response.param.status = "Invalid amount"
        } else {
            check_result = 0;
            checkMoney;
            if(check_result == 0){
                response.param.status = "Cannot withdraw!"
            } else {
                //There are enought money! Let's withdraw
                amount = -1 * amount;
                depositMoney;
                response.param.status = "Executed"
            }
        }
        

    }]

    [ putDeposit( request )( response ) {
        account = request.param.account;
        amount = request.param.amount;
        if(amount < 0) {
            response.param.status = "Invalid amount"
        } else {
            check_result = 0;
            checkAccount;
            if(check_result == 0){
                response.param.status = "Invalid account"
            } else {
                depositMoney;
                response.param.status = "Executed"
            }
        }
    }]

    [ getAccount( request )( response ) {
        pageSize = 10;
        pageNo = (request.param.page) - 1;
        if(pageNo < 0) {
            pageNo = 0
        }
        offset = pageNo * pageSize;
        getAccounts;
        response.param = {};
        response.param.array = {};
        undef(response.param.array[0])
        for ( i = 0, i < #queryReturn.row, i++ ) {
            response.param.array[i] << output[i]
        }
    }]

    
}

