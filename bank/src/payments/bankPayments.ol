include "BankPaymentsI.iol"

include "console.iol"
include "string_utils.iol"
include "database.iol"

execution{ concurrent }



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
}

main {
    [ getCheckPay( request )( response ) {
        response.param.amount = 10.5
        response.param.status = "Ok"
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