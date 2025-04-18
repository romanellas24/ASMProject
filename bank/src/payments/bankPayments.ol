include "../gateway/BankGatewayInterface.iol"
include "BankPaymentsI.iol"
include "BPUtils.ol"

include "console.iol"
include "string_utils.iol"
include "database.iol"

execution{ concurrent }

inputPort BankPaymentsPort {
    Location: "socket://localhost:9002"
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
        .host = "localhost";
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
        "AND PAN = :pan AND cvv = :cvv AND owner = :fullname AND MONTH(expiry_on) = :expire_month AND YEAR(expiry_on) = :expire_year";
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
        cnt = queryReturn.row[0].a
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
                response.param.status = "Payment sent not valid, but semantically correct.";
                response.param.code = 400
            } else {
                //TODO CONTINUE HERE!
                response.param.status = "Ok.";
                response.param.code = 200
            }
            
        }

    }]

    /*

    [ postPay( request )( response ) {
        response.status = "Ok"
    }]

    [deleteRefund(request)(response) {
        response.status = "Ok"
    }]
    */
}

