execution { concurrent }

include "bankPayPageI.iol"
include "console.iol"
include "file.iol"

include "../locations.ol"

inputPort HttpInput {
    Location: LOCATIONS_STATIC
    Protocol: http { 
        .format = "html",
        .method = "get",
        .contentType = "text/html"
        }
    Interfaces: HttpInterface
}

init {
    println@Console("Bank Payments Page is running...")()
}

main
{
    [ payForm(token)( output ) {

        with(fileRequest) {
            .filename = "content/pay.html"
            .format = "text/html"
        }
        readFile@File(fileRequest)(content);
        output = content
    }]{ nullProcess }
}