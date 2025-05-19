include "../gateway/bankGatewayI.iol"
include "BankPaymentsI.iol"
include "BPUtils.ol"

include "console.iol"
include "string_utils.iol"
include "database.iol"

include "../locations.ol"

execution{ concurrent }

inputPort BankPaymentsPort {
    Location: "socket://localhost:9002/"
    Protocol: xmlrpc { 
        .compression = false
    }
    Interfaces: BankPaymentsI
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
    println@Console("Bank Payments is running...")()
}

//Reqires: seed, token
define askForTokenProcedure {
    scope ( askForToken ) {
        sql_cmd = "SELECT CREATE_TOKEN(:seed, CURRENT_TIMESTAMP()) AS a";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("Params: (Ordered)")();
            println@Console("seed:" + seed)();
            println@Console(askForToken.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        requestTokenQuery = sql_cmd;
        requestTokenQuery.seed = seed;
        query@Database( requestTokenQuery )( queryReturn );
        token = queryReturn.row[0].a
    }
}


//Requires: amount, destination_account, token
define populateTokenProcedure
{
    scope ( populateTokenScope ) {
        sql_cmd = "UPDATE tbl_tokens SET amount = :amount, dest_account = :dest_account WHERE token = :token";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("Params: (Ordered)")();
            println@Console("amount:" + amount)();
            println@Console("dest_account:" + dest_account)();
            println@Console("token:" + token)();
            println@Console("Throwed:")();
            println@Console(populateTokenScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        updateQueryRequest = sql_cmd;
        updateQueryRequest.amount = amount;
        updateQueryRequest.dest_account = dest_account;
        updateQueryRequest.token = token;
        update@Database( updateQueryRequest )( queryReturn )
    }
}

//Requires: token, pan, cvv, fullname, expire_month, expire_year, cnt
define checkPaymentData
{
    scope ( checkPaymentDataScope ) {
        sql_cmd = "SELECT COUNT(*) AS cnt " + 
        "FROM tbl_debit_cards AS cards " +
        "INNER JOIN tbl_account AS accounts ON (accounts.id = cards.refer_account) " +
        "INNER JOIN view_balances AS balances ON (balances.account = accounts.id) " +
        "WHERE EXISTS (SELECT * FROM tbl_tokens WHERE token = :token AND amount <= balances.balance) " +
        "AND PAN = :pan AND cvv = :cvv AND UPPER(owner) = UPPER(:fullname) AND MONTH(expiry_on) = :expire_month AND YEAR(expiry_on) = :expire_year AND expiry_on > CURRENT_TIMESTAMP() " + 
        "AND NOT EXISTS(SELECT token FROM tbl_token_transactions WHERE token = :token )";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("token:" + token)();
            println@Console("pan:" + pan)();
            println@Console("cvv:" + cvv)();
            println@Console("fullname:" + fullname)();
            println@Console("expire_month:" + expire_month)();
            println@Console("expire_year:" + expire_year)();
            println@Console("Throwed:")();
            println@Console(checkPaymentDataScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.token = token;
        queryRequest.pan = pan;
        queryRequest.cvv = cvv;
        queryRequest.fullname = fullname;
        queryRequest.expire_month = expire_month;
        queryRequest.expire_year = expire_year;
        query@Database( queryRequest )( queryReturn );
        cnt = queryReturn.row[0].cnt
    }
}

//Requires: token, pan, error
define registerPayment
{
    error = 0
    scope ( registerPaymentScope ) {
        sql_cmd = "INSERT INTO tbl_token_transactions (token, card) VALUES (:token, :pan)";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("token:" + token)();
            println@Console("pan:" + pan)();
            println@Console(registerPaymentScope.SQLException.Error)();
            error = 1;
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.token = token;
        queryRequest.pan = pan;
        update@Database( queryRequest )( queryReturn )
    }
}

//Requires: token, result, beneficiary, amount, toDelete
/*
result = 0 => "Not payed"
result = 1 => "Payed"
result = -1 => "Not exists"
*/
define checkToken
{
    adding = "";
    if(toDelete){
        adding = "AND deletable = 1 "
    }
    scope ( checkTokenScope ) {
        sql_cmd = "SELECT tbl_tokens.token, COUNT(tbl_token_transactions.token) AS cnt, tbl_account.owner AS beneficiary, tbl_tokens.amount " +
                   "FROM tbl_tokens " +
                    "LEFT JOIN tbl_token_transactions ON (tbl_tokens.token = tbl_token_transactions.token) " +
                    "INNER JOIN tbl_account ON (tbl_tokens.dest_account = tbl_account.id) " +
                    "WHERE tbl_tokens.token = :token " + adding + 
                    "GROUP BY tbl_tokens.token";                    
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("token:" + token)();
            println@Console(checkTokenScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.token = token;
        query@Database( queryRequest )( queryReturn );
        result = -1;
        if(#queryReturn.row > 0){
            if(queryReturn.row[0].cnt == 0){
                result = 0
            } else {
                result = 1
            }
            beneficiary = queryReturn.row[0].beneficiary;
            amount = queryReturn.row[0].amount
        }
    }
}

//Requires: token
define deleteTransaction
{
    scope ( deleteTransactionScope ) {
        sql_cmd = "DELETE FROM tbl_token_transactions WHERE token = :token AND deletable = 1";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("token:" + token)();
            println@Console(deleteTransactionScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.token = token;
        update@Database( queryRequest )( queryReturn )
    }
}

//token
define makeNotRefundable 
{
    scope ( makeNotRefundableScope ) {
        error = 0;
        sql_cmd = "UPDATE tbl_token_transactions SET deletable = 0 WHERE token = :token";
        install ( SQLException =>
            error = 1;
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("token:" + token)();
            println@Console(makeNotRefundableScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.token = token;
        update@Database( queryRequest )( queryReturn )
    }
}

main {
    [ postPay( request )( response ) {
        //Generate random seed
        generateRandomSeed;
        //Storing params
        amount = request.param.amount;
        dest_account = request.param.dest_account;
        //Asking for token
        askForTokenProcedure;
        //Update the generated token (here variable token is defined and populated)
        populateTokenProcedure
        //Returns token
        response.param.token = token
    }]


    [ putPay( request )( response ) {
        pan = request.param.pan;
        cvv = request.param.cvv;
        expire_month = request.param.expire_month;
        expire_year = request.param.expire_year;
        card_holder_first_name = request.param.card_holder_first_name;
        card_holder_last_name = request.param.card_holder_last_name;
        token = request.param.token;

        expire_year = expire_year + 2000;

        if(pan == "" || 
                cvv <= 0 || 
                expire_month <= 0 || 
                expire_month > 12 || 
                expire_year < 2000 || 
                card_holder_first_name == "" || 
                card_holder_last_name == "" || 
                token == "") {
            response.param.status = "Invalid params";
            response.param.code = 400
        } else {
            fullname = card_holder_first_name + " " + card_holder_last_name;
            undef(card_holder_first_name);
            undef(card_holder_last_name);
            trim@StringUtils(fullname)(fullname);
            cnt = 0;
            checkPaymentData;
            if(cnt != 1){
                response.param.status = "Payment information sent not valid, but semantically correct.";
                response.param.code = 400
            } else {
                registerPayment;
                if(error == 0) {
                    response.param.status = "Ok.";
                    response.param.code = 200
                } else {
                    response.param.status = "Internal server error";
                    response.param.code = 500
                }
                
            }   
        }
    }]

    
    [ getCheckPay( request )( response ) {
        token = request.param.token;
        response.param.beneficiary = "";
        amount = 0.00;
        toDelete = false;
        checkToken;
        if(result == -1) {
            response.param.status = "Not found";
            response.param.code = 404;
            response.param.amount = amount
        } else if (result == 0) {
            response.param.status = "Not paid";
            response.param.code = 200;
            response.param.beneficiary = beneficiary;
            response.param.amount = amount
        } else {
            //result == 1
            response.param.status = "Paid";
            response.param.code = 200;
            response.param.beneficiary = beneficiary;
            response.param.amount = amount
        }
    }]

    [deletePay(request)(response) {
        token = request.param.token;
        toDelete = true;
        checkToken;
        if(result == -1) {
            response.param.status = "Not found";
            response.param.code = 404
        } else if (result == 0) {
            response.param.status = "Not paid";
            response.param.code = 400
        } else {
            //result == 1, We have to refound. (Deleting transaction)
            deleteTransaction;
            response.param.status = "Deleted";
            response.param.code = 200
        }
    }]

    [putNotRefaundable(request)(response) {
        token = request.param.token;
        makeNotRefundable;
        response.param.status = 200;
        response.param.msg = "Ok.";
        if(error){
            response.param.status = 500;
            response.param.msg = "Cannot update!"
        }
    }]

}

