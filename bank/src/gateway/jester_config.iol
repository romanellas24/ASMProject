type GetCheckPayRequest:void {
  .transactionId[1,1]:string
}

type GetCheckPayResponse:void {
  .amount[1,1]:double
  .status[1,1]:string
}

type PostPayRequest:void {
  .cvv[1,1]:int
  .expire_month[1,1]:int
  .card_holder_first_name[1,1]:string
  .expire_year[1,1]:int
  .pan[1,1]:string
  .card_holder_last_name[1,1]:string
}

type PostPayResponse:void {
  .status[1,1]:string
}

type RefundRequest:void {
  .token[1,1]:string
}

type RefundResponse:void {
  .status[1,1]:string
}

interface BANK_GATEWAYInterface {
RequestResponse:
  deleteRefund( RefundRequest )( RefundResponse ),
  getCheckPay( GetCheckPayRequest )( GetCheckPayResponse ),
  postPay( PostPayRequest )( PostPayResponse )
}



outputPort BANK_GATEWAY {
  Protocol:sodep
  Location:"local"
  Interfaces:BANK_GATEWAYInterface
}


embedded { Jolie: "bankGateway.ol" in BANK_GATEWAY }
