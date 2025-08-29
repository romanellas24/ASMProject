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

type AccountRequestBe: void {
    .param: AccountRequest
}

type AccountResponseBe: void {
    .param: AccountResponse
}

type TransactionsRequestBe: void {
    .param: TransactionsRequest
}

type TransactionsResponseBe: void {
    .param: TransactionsResponse
}

type CreateCardRequestBe: void {
    .param: CreateCardRequest
}

type CreateCardResponseBe: void {
    .param: CreateCardResponse
}

type AccountExistsRequestBe: void {
    .param: AccountExistsRequest
}

type AccountExistsResponseBe: void {
    .param: AccountExistsResponse
}

interface BankAccountsI {
    RequestResponse:
        postAccount(PostAccountRequestBe)(PostAccountResponseBe),
        putWithdraw(WithdrawRequestBe)(WithdrawResponseBe),
        putDeposit(DepositRequestBe)(DepositResponseBe),
        getAccount(AccountRequestBe)(AccountResponseBe),
        getTransactions(TransactionsRequestBe)(TransactionsResponseBe),
        postCreateCard(CreateCardRequestBe)(CreateCardResponseBe),
        getAccountExists(AccountExistsRequestBe)(AccountExistsResponseBe)
}