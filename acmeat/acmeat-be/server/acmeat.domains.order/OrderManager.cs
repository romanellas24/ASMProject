
using System.Collections.Generic;
using acmeat.server.order.dataproxy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace acmeat.server.order.manager{


[Route("api/[controller]/[action]")]
[ApiController]
public class OrderManager: ControllerBase{

    private readonly ILogger<OrderManager> _logger;
    private readonly acmeat.server.order.dataproxy.OrderReader _orderReader;

    public OrderManager(
        ILogger<OrderManager> logger,
        OrderReader orderReader){

        _logger = logger;    
        _orderReader = orderReader;
    }

    [HttpGet]
    public List<Order>GetOrders(){
        _logger.LogInformation($"Getting Orders");
        return _orderReader.GetOrders();
       
    }

}

}
