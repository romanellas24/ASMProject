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

interface BankPaymentsInterface {
    RequestResponse:
        getCheckPay(GetCheckPayRequest)(GetCheckPayResponse),
        postPay(PostPayRequest)(PostPayResponse),
        deleteRefund(RefundRequest)(RefundResponse)
}

inputPort BankPaymentsPort {
    Location: "socket://localhost:9002"
    Protocol: xmlrpc { .debug = true }
    Interfaces: BankPaymentsInterface
}