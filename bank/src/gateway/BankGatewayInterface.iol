type GetCheckPayRequest: void {
    .transactionId: string
}

type GetCheckPayResponse: void {
    .amount: double
    .status: string
}

type PostPayRequest: void {
    .pan: string
    .cvv: int
    .expire_month: int
    .expire_year: int
    .card_holder_first_name: string
    .card_holder_last_name: string
    .dest_account: int
    .amount: double
}

type PostPayResponse: void {
    .status: string
}

type RefundRequest: void {
    .token: string
}

type RefundResponse: void {
    .status: string
}

interface BankGatewayInterface {
    RequestResponse:
        getCheckPay(GetCheckPayRequest)(GetCheckPayResponse),
        postPay(PostPayRequest)(PostPayResponse),
        deleteRefund(RefundRequest)(RefundResponse)
}

inputPort BANK_GATEWAYServicePort {
    Location: "socket://localhost:9001"
    Protocol: soap {
	    .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }
    Interfaces: BankGatewayInterface
}