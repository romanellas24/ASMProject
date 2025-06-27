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
    Location: "socket://localhost:9001"
    Protocol: soap {
        .wsdl = "./wsdl.xml"
	    .wsdl.port = "BankGatewayInterface"
	    .namespace = "joliebank.romanellas.cloud"
	    .debug = true
	    .compression = false
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

    [getTransactions(request)(response) {
        internalReq.param << request;
        getTransactions@BankAccountsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [putNotRefaundable(request)(response) {
        internalReq.param << request;
        putNotRefaundable@BankPaymentsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

    [postCreateCard(request)(response) {
        internalReq.param << request;
        postCreateCard@BankAccountsPort(internalReq)(internalRes);
        response << internalRes.param
    }]

}