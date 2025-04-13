include "BankClient.iol"
include "console.iol"

main
{
    request.transactionId = "AAAA-BBBB-CCCC-DDDD";
    getCheckPay@BANK_GATEWAYServicePort( request )( response );
    println@Console( response.status )()
}