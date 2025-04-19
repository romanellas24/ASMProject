include "console.iol"
include "BankGatewayInterface.iol"
include "../payments/BankPaymentsI.iol"

outputPort BANK_GATEWAY {
    Location: "socket://localhost:9001"
    Protocol: soap {
        .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }
    Interfaces: BankGatewayInterface
}

main
{
    //Creating token
    println@Console("Creating token...")();
    request.amount = 15.26;
    request.dest_account = 2;
    postPay@BANK_GATEWAY( request )( response );
    token = response.token;
    println@Console( "Token:" + token )()
    undef(request);
    undef(response);
    //Paying - Invalid params
    request.pan = "0101020203030404";
    request.cvv = 250;
    request.expire_month = 9;
    request.expire_year = 2075;
    request.card_holder_first_name = "";
    request.card_holder_last_name = "Romanella";
    request.token = token;
    putPay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )()
    undef(request);
    undef(response);
    //Paying - Invalid card - payment info
    request.pan = "0101020203030404";
    request.cvv = 250;
    request.expire_month = 9;
    request.expire_year = 2075;
    request.card_holder_first_name = "Daniele";
    request.card_holder_last_name = "Romanella";
    request.token = token + "AAA";
    putPay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )()
    undef(request);
    undef(response);
    //Paying valid info
    request.pan = "5353530123456789";
    request.cvv = 123;
    request.expire_month = 5;
    request.expire_year = 2025;
    request.card_holder_first_name = "Daniele";
    request.card_holder_last_name = "Romanella";
    request.token = token;
    putPay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )();
    undef(request);
    undef(response);
    //Paying check - All OK
    request.token = token;
    getCheckPay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )();
    undef(request);
    undef(response);
    //Paying check - Undefined token
    request.token = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    getCheckPay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )();
    undef(request);
    undef(response);
    //Paying check - Unpaid token
    request.amount = 90;
    request.dest_account = 1;
    postPay@BANK_GATEWAY( request )( response );
    notPaid = response.token;
    undef(request);
    undef(response);
    request.token = notPaid;
    getCheckPay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )();
    undef(request);
    undef(response);
    //Refund
    request.token = token
    deletePay@BANK_GATEWAY( request )( response );
    println@Console( "Response: " + response.status )();
    undef(request);
    undef(response)
}