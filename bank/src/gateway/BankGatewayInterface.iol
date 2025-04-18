type GetCheckPayRequest: void {
    .transactionId: string
}

type GetCheckPayResponse: void {
    .amount: double
    .status: string
}

type PostPayRequest: void {
    .amount: double
    .dest_account: int
}

type PostPayResponse: void {
    .token: string
}

type PutPayRequest: void {
    .pan: string
    .cvv: int
    .expire_month: int
    .expire_year: int
    .card_holder_first_name: string
    .card_holder_last_name: string
    .token: string
}

type PutPayResponse: void {
    .status: string
    .code: int
}

type RefundRequest: void {
    .token: string
}

type RefundResponse: void {
    .status: string
}

type GetCheckPayRequestBe: void {
    .param: GetCheckPayRequest
}

type GetCheckPayResponseBe: void {
    .param: GetCheckPayResponse
}

interface BankGatewayInterface {
    RequestResponse:
        getCheckPay(GetCheckPayRequest)(GetCheckPayResponse),
        postPay(PostPayRequest)(PostPayResponse),
        putPay(PutPayRequest)(PutPayResponse),
        deleteRefund(RefundRequest)(RefundResponse)
}


/*
    {
	    .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }*/