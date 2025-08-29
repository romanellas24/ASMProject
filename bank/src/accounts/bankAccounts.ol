include "../gateway/bankGatewayI.iol"
include "BankAccountsI.iol"

include "console.iol"
include "string_utils.iol"
include "json_utils.iol"
include "database.iol"
include "../locations.ol"
include "../shared.ol"

execution{ concurrent }

inputPort BankAccountsPort {
    Location: "socket://localhost:9003/"
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

//pageSize, offset, acc_id
define getTransactions 
{
    scope ( getAccountsScope ) {
        sql_cmd = "SELECT token, amount, src_account, dest_account, payment_request_time, transaction_on, src_owner, dest_owner, deletable FROM view_accounts_transactions WHERE src_account = :id OR dest_account = :id LIMIT :offset, :pageSize";
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
        queryRequest.id = acc_id;
        queryRequest.pageSize = pageSize;
        queryRequest.offset = offset;
        query@Database( queryRequest )( queryReturn );
        
        for ( i = 0, i < #queryReturn.row, i++ ) {
            output[i].token = queryReturn.row[i].token;
            output[i].amount = queryReturn.row[i].amount;
            output[i].src_account = queryReturn.row[i].src_account;
            output[i].dest_account = queryReturn.row[i].dest_account;
            output[i].payment_request_time = queryReturn.row[i].payment_request_time;
            output[i].transaction_on = queryReturn.row[i].transaction_on;
            output[i].src_owner = queryReturn.row[i].src_owner;
            output[i].dest_owner = queryReturn.row[i].dest_owner;
            output[i].deletable = queryReturn.row[i].deletable
        }
    }
}

//pan, check_result
define checkPan 
{
    scope ( checkPanScope ) {
        sql_cmd = "SELECT COUNT(*) AS cnt FROM tbl_debit_cards WHERE PAN = :pan";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("pan:" + pan)();
            println@Console(checkPanScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.pan = pan;
        query@Database( queryRequest )( queryReturn );
        if(#queryReturn.row > 0){
            if(queryReturn.row[0].cnt != 0){
                check_result = queryReturn.row[0].cnt
            }
        }
    }
}


//account, pan, cvv, expire_month, expire_year
define insertCard 
{
    scope ( insertCardScope ) {
        error = 0;
        sql_cmd = "INSERT INTO tbl_debit_cards (PAN, cvv, expiry_on, refer_account) VALUE (:pan, :cvv, LAST_DAY(CONCAT(:expire_year,'-',:expire_month,'-',01)), :account)";
        install ( SQLException =>
            error = 1;
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("pan:" + pan)();
            println@Console("cvv:" + cvv)();
            println@Console("expire_month:" + expire_month)();
            println@Console("expire_year:" + expire_year)();
            println@Console("account:" + account)();
            println@Console(insertCardScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.pan = pan;
        queryRequest.cvv = cvv;
        queryRequest.expire_year = expire_year;
        queryRequest.expire_month = expire_month;
        queryRequest.account = account;
        update@Database( queryRequest )( queryReturn )
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

    [ getTransactions( request )( response ) {
        pageSize = 10;
        pageNo = (request.param.page) - 1;
        acc_id = request.param.acc_id;
        if(pageNo < 0) {
            pageNo = 0
        }
        offset = pageNo * pageSize;
        getTransactions;
        response.param = {};
        response.param.array = {};
        undef(response.param.array[0])
        for ( i = 0, i < #queryReturn.row, i++ ) {
            response.param.array[i] << output[i]
        }
    }]

    [ postCreateCard( request )( response ) {
        account = request.param.acc_id
        pan = request.param.pan;
        cvv = request.param.cvv;
        expire_month = request.param.expire_month;
        expire_year = request.param.expire_year;
        expire_year = "20" + expire_year;
        check_result = 0;
        checkAccount;
        if(check_result == 0) {
            response.param.status = 400;
            response.param.msg = "Account does not exists"
        } else {
            //Account exists, check pan
            check_result = 0;
            checkPan;
            if(check_result == 0){
                //Insert
                insertCard;
                if(error == 0){
                    response.param.status = 200;
                    response.param.msg = "Ok."
                } else {
                    response.param.status = 500;
                    response.param.msg = "Internal server error"    
                }
                
            } else {
                //Already exists!
                response.param.status = 400;
                response.param.msg = "PAN already exists"
            }
        }
    }]

    [getAccountExists(request)(response) {
        account = request.param.account;
        check_result = 0;
        checkAccount;
        response.param.status = 200;
        response.param.exists = int(check_result)
    }]

    
}

