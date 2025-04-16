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
    request.amount = 15.26;
    request.dest_account = 1;
    postPay@BANK_GATEWAY( request )( response );
    println@Console( response.status )()
}