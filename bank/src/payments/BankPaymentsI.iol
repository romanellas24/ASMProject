type PostPayRequestBe: void {
    .param: PostPayRequest
}

type PostPayResponseBe: void {
    .param: PostPayResponse
}

interface BankPaymentsI {
    RequestResponse:
        postPay(PostPayRequestBe)(PostPayResponseBe)
        
}