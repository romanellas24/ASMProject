type incomingHeaderHandlerRequest:void {
    .operation:string 
    .headers:undefined
}
type incomingHeaderHandlerResponse: undefined

type outgoingHeaderHandlerRequest:void {
    .operation:string 
    .response?:undefined
}
type outgoingHeaderHandlerResponse: undefined

interface HeaderHandlerInterface {
RequestResponse:
    incomingHeaderHandler( incomingHeaderHandlerRequest )( incomingHeaderHandlerResponse ),
    outgoingHeaderHandler( outgoingHeaderHandlerRequest )( outgoingHeaderHandlerResponse )
}

inputPort HeaderPort {
    Protocol: "sodep"
    Location: "local"
    Interfaces: HeaderHandlerInterface
}

execution { concurrent }

main {
    [incomingHeaderHandler(request)(response){
        nullProcess
    }]

    [outgoingHeaderHandler(request)(response){
        response.("Access-Control-Allow-Methods") = "POST,GET,DELETE,PUT,OPTIONS";
        response.("Access-Control-Allow-Origin") = "*"
    }]
}