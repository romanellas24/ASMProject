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

type GetCheckPayRequestBe : void {
    .param: GetCheckPayRequest
}

type GetCheckPayResponseBe : void {
    .param: GetCheckPayResponse
}

type RefundRequestBe: void {
    .param: RefundRequest
}

type RefundResponseBe: void {
    .param: RefundResponse
}

type NotRefoundRequestBe: void {
    .param: NotRefoundRequest
}

type NotRefoundResponseBe: void {
    .param: NotRefoundResponse
}

interface BankPaymentsI {
    RequestResponse:
        postPay(PostPayRequestBe)(PostPayResponseBe),
        putPay(PutPayRequestBe)(PutPayResponseBe),
        getCheckPay(GetCheckPayRequestBe)(GetCheckPayResponseBe),
        deletePay(RefundRequestBe)(RefundResponseBe),
        putNotRefaundable(NotRefoundRequestBe)(NotRefoundResponseBe)
        
}