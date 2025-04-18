type PostPayRequestBe: void {
    .param: PostPayRequest
}

type PostPayResponseBe: void {
    .param: PostPayResponse
}

type PutPayRequestBe : void {
    .param: PutPayRequest
}

type PutPayResponseBe : void {
    .param: PutPayResponse
}

interface BankPaymentsI {
    RequestResponse:
        postPay(PostPayRequestBe)(PostPayResponseBe),
        putPay(PutPayRequestBe)(PutPayResponseBe)
        
}