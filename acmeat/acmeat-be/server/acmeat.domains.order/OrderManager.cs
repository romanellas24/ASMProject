
using System.Collections.Generic;
using System.Threading.Tasks;
using acmeat.server.order.dataproxy;
using acmeat.server.order.datawriter;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace acmeat.server.order.manager{



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
[Route("api/[controller]/[action]")]
[ApiController]
public class OrderManager: ControllerBase{

    private readonly ILogger<OrderManager> _logger;
    private readonly OrderReader _orderReader;
    private readonly OrderDataWriter _orderDataWriter;

    public OrderManager(
        ILogger<OrderManager> logger,
        OrderReader orderReader,
        OrderDataWriter orderDataWriter){

        _logger = logger;    
        _orderReader = orderReader;
        _orderDataWriter = orderDataWriter;
    }

    [HttpGet]
    public List<Order>GetOrders(){
        _logger.LogInformation($"Getting Orders");
        return _orderReader.GetOrders();
       
    }

    [HttpGet("{id}")]
    public Order GetOrderById(int id){
        _logger.LogInformation($"Getting order with id:{id}");
        return _orderReader.GetOrderById(id);
    }

    [HttpPost]
    public async Task CreateOrder(Order order){
        _logger.LogInformation($"Creating order with id:{order.Id}");
        await _orderDataWriter.SendAsync(new CreateNewOrderCommand(order));
    }


    [HttpPatch]
    public async Task UpdateOrder(Order order){
        _logger.LogInformation($"Updating order with id:{order.Id}");
        await _orderDataWriter.SendAsync(new UpdateNewOrderCommand(order));
    }

}

}
