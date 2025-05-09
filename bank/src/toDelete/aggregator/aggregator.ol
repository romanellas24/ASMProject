include "aggregatorI.iol"
include "../static/bankPayPageI.iol"
include "../gateway/bankGatewayI.iol"
include "console.iol"
include "../locations.ol"

outputPort HttpInput {
	location: LOCATIONS_STATIC
	Protocol: http { 
        .format = "html",
        .method = "get",
        .contentType = "text/html"
        }
	interfaces: HttpInterface
}

outputPort ApiGateway {
	location: LOCATIONS_API_GATEWAY_SODEP
	Protocol: http { 
        .debug = true
        }
	interfaces: BankGatewayInterface
}

/* this is the inputPort of the Aggregation service */
inputPort Aggregator {
	location: LOCATIONS_AGGREGATOR
	Protocol: http { 
        .format = "html",
        .contentType = "text/html",
		.debug = true
        }
	interfaces: AggregatorInterface
	aggregates: ApiGateway, HttpInput
}
init {
    println@Console("Bank aggregator is running...")()
}


main
{
    [isAlive (void)(response){
		response = 1
	}]
}