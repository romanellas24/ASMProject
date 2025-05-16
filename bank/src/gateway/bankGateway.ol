include "bankGatewayI.iol"
include "../payments/BankPaymentsI.iol"
include "../accounts/BankAccountsI.iol"

include "console.iol"
include "string_utils.iol"
include "database.iol"
include "json_utils.iol"

include "../locations.ol"

execution{ concurrent }

inputPort BANK_GATEWAY {
    Location: "local"
    Protocol: sodep
    Interfaces: BankGatewayInterface
}

inputPort BANK_GATEWAY_2 {
    Location: LOCATIONS_API_GATEWAY_SOAP
    //Location: "local"
    //Protocol: sodep
    Protocol: soap {
        .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }
    Interfaces: BankGatewayInterface
}

outputPort BankPaymentsPort {
    Location: LOCATIONS_API_PAYMENTS
    Protocol: xmlrpc { 
        .compression = false
    }
    Interfaces: BankPaymentsI
}

outputPort BankAccountsPort {
    Location: LOCATIONS_API_ACCOUNTS
    Protocol: xmlrpc { 
        .compression = false
        .debug = false
    }
    Interfaces: BankAccountsI
}

init {
    println@Console("Bank is running...")()
}

main {
    [ getCheckPay( request )( response ) {
        internalReq.param << request;
        getCheckPay@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [ postPay( request )( response ) {
        internalReq.param << request;
        postPay@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [ putPay( request )( response ) {
        internalReq.param << request;
        putPay@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [deletePay(request)(response) {
        internalReq.param << request;
        deletePay@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [postAccount(request)(response) {
        internalReq.param << request;
        postAccount@BankAccountsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [putWithdraw(request)(response) {
        internalReq.param << request;
        putWithdraw@BankAccountsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [putDeposit(request)(response) {
        internalReq.param << request;
        putDeposit@BankAccountsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [getAccount(request)(response) {
        internalReq.param << request;
        getAccount@BankAccountsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

}