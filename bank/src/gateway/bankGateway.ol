include "BankGatewayInterface.iol"
include "../payments/BankPaymentsI.iol"

include "console.iol"
include "string_utils.iol"
include "database.iol"

execution{ concurrent }

inputPort BANK_GATEWAY {
    Location: "socket://localhost:9001"
    Protocol: soap {
        .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }
    Interfaces: BankGatewayInterface
}

outputPort BankPaymentsPort {
    Location: "socket://localhost:9002"
    Protocol: xmlrpc { 
        .compression = false
    }
    Interfaces: BankPaymentsI
}

init {
    println@Console("Bank is running...")()
}

main {
    [ getCheckPay( request )( response ) {
        response.status = "Ok"
    }]

    [ postPay( request )( response ) {
        //internalReq.param.dest_account = request.dest_account;
        //internalReq.param.amount = request.amount;
        internalReq.param << request;
        postPay@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [ putPay( request )( response ) {
        internalReq.param << request;
        putPay@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [deleteRefund(request)(response) {
        response.status = "Ok"
    }]
}