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

//Reqires: seed
define askForToken {
    
}


//Requires: amount, destination_account, seed
define insertTokenProcedure
{
    scope ( insertTokenScope ) {
        sql_cmd = "INSERT INTO tbl_tokens (amount, dest_account, token) VALUES (:amount, :dest_account, :token);";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("Params: (Ordered)")();
            println@Console("amount:" + amount)();
            println@Console("dest_account:" + dest_account)();
            println@Console("token:" + token)();
            println@Console("Throwed:")();
            println@Console(insertTokenScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        insertQueryRequest = sql_cmd;
        insertQueryRequest.amount = amount;
        insertQueryRequest.dest_account = dest_account;
        update@Database( insertQueryRequest )( queryReturn )
    }
}

main {
    [ postPay( request )( response ) {
        generateRandomSeed;
        amount = request.param.amount;
        dest_account = request.param.dest_account;
        insertTokenProcedure
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

