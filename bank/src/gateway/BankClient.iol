type NOTATIONType:any

type postPayResponse:void {
	.status:string
}

type getCheckPayResponse:void {
	.amount:double
	.status:string
}

type getCheckPay:void {
	.transactionId:string
}

type postPay:void {
	.cvv:int
	.dest_account:int
	.amount:double
	.expire_month:int
	.card_holder_first_name:string
	.expire_year:int
	.pan:string
	.card_holder_last_name:string
}

type deleteRefund:void {
	.token:string
}

type deleteRefundResponse:void {
	.status:string
}

interface BANK_GATEWAY {
RequestResponse:
	getCheckPay(getCheckPay)(getCheckPayResponse),
	postPay(postPay)(postPayResponse),
	deleteRefund(deleteRefund)(deleteRefundResponse)
}

outputPort BANK_GATEWAYServicePort {
Location: "socket://localhost:9001"
Protocol: soap {
	.wsdl = "file:/home/romanellas/WebstormProjects/ASMProject/bank/src/gateway/wsdl.xml";
	.wsdl.port = "BANK_GATEWAYServicePort"
}
Interfaces: BANK_GATEWAY
}

