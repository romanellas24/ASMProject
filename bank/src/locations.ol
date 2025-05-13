constants {
    LOCATIONS_API_GATEWAY_SOAP = "socket://joliebank-gw-soap-service.default.svc.cluster.local:9001/",                  //OK
    LOCATIONS_API_PAYMENTS = "socket://joliebank-payments-service.default.svc.cluster.local:9002/",                     //OK
    LOCATIONS_API_ACCOUNTS = "socket://joliebank-accounts-service.default.svc.cluster.local:9003/",                     //OK
    LOCATIONS_STATIC = "socket://joliebank-static-service.default.svc.cluster.local:9004/",                             //OK
    LOCATIONS_API_GATEWAY_HTTP = "socket://joliebank-gw-http-service.default.svc.cluster.local:9005/",                  //OK
    LOCATIONS_AGGREGATOR = "socket://joliebank-gw-http-service.default.svc.cluster.local:9006/", //USED IN Ngnix
    // MYSQL_HOST = "192.168.24.5"
    MYSQL_HOST = "joliebank-mysql-service.default.svc.cluster.local"                                                    // OK
}