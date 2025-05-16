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

type AccountRequest: void {
    .page: int
}

type AccountInfo: void {
    .account_id: int
    .owner: string
    .balance: double
}

type AccountResponse: void {
    .account_list[0,*]: AccountInfo
}

type TransactionsRequest: void {
    .acc_id: int
}

type TransactionInfo: void {
    .owner: string
    .variation_value: double
    .on_date: string
}

type TransactionsResponse: void {
    .transaction_list[0,*]: TransactionInfo
}

type CreateCardRequest: void {
    .acc_id: int
}

type CreateCardResponse: void {
    .status: int
    .msg: string
}

type NotRefoundRequest: void {
    .token: string
}

type NotRefoundResponse: void {
    .status: int
    .msg: string
}


interface BankGatewayInterface {
    RequestResponse:
        getCheckPay(GetCheckPayRequest)(GetCheckPayResponse),
        postPay(PostPayRequest)(PostPayResponse),
        putPay(PutPayRequest)(PutPayResponse),
        deletePay(RefundRequest)(RefundResponse),
        postAccount(PostAccountRequest)(PostAccountResponse),
        putWithdraw(WithdrawRequest)(WithdrawResponse),
        putDeposit(DepositRequest)(DepositResponse),
        getAccount(AccountRequest)(AccountResponse)
        /*
        ,
        getTransactions(TransactionsRequest)(TransactionsResponse),
        postCreateCard(CreateCardRequest)(CreateCardResponse),
        putNotRefaundable(NotRefoundRequest)(NotRefoundResponse)
        */
}


/*
    {
	    .wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	    .wsdl.port = "BankGatewayInterface"
    }*/