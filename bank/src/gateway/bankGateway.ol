include "BankGatewayInterface.iol"

include "console.iol"
include "string_utils.iol"

execution{ concurrent }

inputPort DEMO {
  Location: "local"
  Protocol: sodep
  Interfaces: BankGatewayInterface
}

execution{ concurrent }

init {
    println@Console("Bank is running...")()
}

main {
    [ getCheckPay( request )( response ) {
      response.amount = 10.5
      response.status = "Ok"
    }]

    [ postPay( request )( response ) {
      response.status = "Ok"
    }]
}