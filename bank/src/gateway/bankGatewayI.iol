type GetCheckPayRequest: void {
    .token: string
}

type GetCheckPayResponse: void {
    .status: string
    .code: int
    .beneficiary: string
    .amount: double
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
    .code: int
}


type PostAccountRequest: void {
    .owner: string
}

type PostAccountResponse: void {
    .identifier: int
}

type WithdrawRequest: void {
    .amount: int
    .account: int
}

type WithdrawResponse: void {
    .status: string
}

type DepositRequest: void {
    .amount: int
    .account: int
}

type DepositResponse: void {
    .status: string
}


interface BankGatewayInterface {
    RequestResponse:
        getCheckPay(GetCheckPayRequest)(GetCheckPayResponse),
        postPay(PostPayRequest)(PostPayResponse),
        putPay(PutPayRequest)(PutPayResponse),
        deletePay(RefundRequest)(RefundResponse),
        postAccount(PostAccountRequest)(PostAccountResponse),
        putWithdraw(WithdrawRequest)(WithdrawResponse),
        putDeposit(DepositRequest)(DepositResponse)
}


/*
    {
	    .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }*/