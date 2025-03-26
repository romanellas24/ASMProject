

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;


namespace acmeat.server.order.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class OrderReader :OrderDao{

    private readonly  MysqlClient _mysqlClient;
    private readonly ILogger<OrderReader> _logger;

    public OrderReader(
        ILogger<OrderReader> logger,
        MysqlClient mysqlClient){

        _logger = logger;    
        _mysqlClient = mysqlClient;
    }
    public List<acmeat.server.order.Order>GetOrders(){
        _logger.LogInformation($"Getting Orders");
       List<acmeat.db.order.Order> orders= _mysqlClient.GetOrders();
        return Utils.ConvertDbListToServerList(orders); 
       
    }
    
    public Order GetOrderById(int id){
        _logger.LogInformation($"Getting Order with id: {id}");
      acmeat.db.order.Order order= _mysqlClient.GetOrderById(id);
        return Utils.ConvertDbElementToServerElement(order); 
       
    }
}