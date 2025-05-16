type AccountInfo:void {
  .owner[1,1]:string
  .account_id[1,1]:int
  .balance[1,1]:double
}

type AccountRequest:void {
  .page[1,1]:int
}

type AccountResponse:void {
  .array[0,*]:AccountInfo
}

type DepositRequest:void {
  .amount[1,1]:int
  .account[1,1]:int
}

type DepositResponse:void {
  .status[1,1]:string
}

type GetCheckPayRequest:void {
  .token[1,1]:string
}

type GetCheckPayResponse:void {
  .amount[1,1]:double
  .code[1,1]:int
  .beneficiary[1,1]:string
  .status[1,1]:string
}

type PostAccountRequest:void {
  .owner[1,1]:string
}

type PostAccountResponse:void {
  .identifier[1,1]:int
}

type PostPayRequest:void {
  .amount[1,1]:double
  .dest_account[1,1]:int
}

type PostPayResponse:void {
  .token[1,1]:string
}

type PutPayRequest:void {
  .cvv[1,1]:int
  .expire_month[1,1]:int
  .card_holder_first_name[1,1]:string
  .expire_year[1,1]:int
  .pan[1,1]:string
  .card_holder_last_name[1,1]:string
  .token[1,1]:string
}

type PutPayResponse:void {
  .code[1,1]:int
  .status[1,1]:string
}

type RefundRequest:void {
  .token[1,1]:string
}

type RefundResponse:void {
  .code[1,1]:int
  .status[1,1]:string
}

type WithdrawRequest:void {
  .amount[1,1]:int
  .account[1,1]:int
}

type WithdrawResponse:void {
  .status[1,1]:string
}

interface BANK_GATEWAYInterface {
RequestResponse:
  deletePay( RefundRequest )( RefundResponse ),
  getAccount( AccountRequest )( AccountResponse ),
  getCheckPay( GetCheckPayRequest )( GetCheckPayResponse ),
  postAccount( PostAccountRequest )( PostAccountResponse ),
  postPay( PostPayRequest )( PostPayResponse ),
  putDeposit( DepositRequest )( DepositResponse ),
  putPay( PutPayRequest )( PutPayResponse ),
  putWithdraw( WithdrawRequest )( WithdrawResponse )
}



outputPort BANK_GATEWAY {
  Protocol:sodep
  Location:"local"
  Interfaces:BANK_GATEWAYInterface
}


embedded { Jolie: "bankGateway.ol" in BANK_GATEWAY }
