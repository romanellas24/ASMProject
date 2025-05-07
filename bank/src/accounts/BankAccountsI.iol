type PostAccountRequestBe: void {
    .param: PostAccountRequest
}

type PostAccountResponseBe: void {
    .param: PostAccountResponse
}

type WithdrawRequestBe: void {
    .param: WithdrawRequest
}

type WithdrawResponseBe: void {
    .param: WithdrawResponse
}

type DepositRequestBe: void {
    .param: DepositRequest
}

type DepositResponseBe: void {
    .param: DepositResponse
}

interface BankAccountsI {
    RequestResponse:
        postAccount(PostAccountRequestBe)(PostAccountResponseBe),
        putWithdraw(WithdrawRequestBe)(WithdrawResponseBe),
        putDeposit(DepositRequestBe)(DepositResponseBe)
}