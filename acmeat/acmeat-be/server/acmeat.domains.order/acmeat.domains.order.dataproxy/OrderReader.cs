

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;


namespace acmeat.server.order.dataproxy;
public class OrderReader{

    private readonly  MysqlClient _mysqlClient;
    private readonly ILogger<OrderReader> _logger;

    public OrderReader(
        ILogger<OrderReader> logger,
        MysqlClient mysqlClient){

        _logger = logger;    
        _mysqlClient = mysqlClient;
    }
    // TO DO: TEST IF IT WORKS 
    public List<acmeat.server.order.Order>GetOrders(){
        _logger.LogInformation($"Getting Orders");
       List<acmeat.db.order.Order> orders= _mysqlClient.GetOrders();
        return Utils.ConvertDbListToServerList(orders); 
       
    }
    
}