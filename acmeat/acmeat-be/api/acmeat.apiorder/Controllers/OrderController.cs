
using acmeat.server.order.client;
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.order
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class OrderController : ControllerBase
    {

        
        private readonly OrderClient _orderClient;
        private readonly ILogger<OrderController> _logger;

        //TO DO INSERT FAKEBANK URL
        private static HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("https://jsonplaceholder.typicode.com"),
        };


         public OrderController(
            OrderClient orderClient,
            ILogger<OrderController> logger

         ){

            _logger = logger;
            _orderClient = orderClient;
            
        }

        [HttpGet("{OrderId}")]
        public async Task<HttpResponseMessage> GetPaymentForm(int OrderId)
        {
            Console.WriteLine($"order id: {OrderId}");
            using HttpResponseMessage response = await sharedClient.GetAsync("paymentOrder/" + OrderId);
            return response;

        }


        [HttpGet("{Id}")]
        public async Task<OrderInfo> GetOrderById(int Id)
        {
            _logger.LogInformation($"Getting order with id: {Id}");
            

            var order = await _orderClient.GetOrderById(Id);
            return new OrderInfo(order);

        }

        [HttpGet]
        public async Task<List<OrderInfo>> GetOrders()
        {
            _logger.LogInformation($"Getting orders ");
            

            var orders = await _orderClient.GetOrderList();
            return orders.Orders.Select(x => new OrderInfo(x)).ToList();

        }

        [HttpGet("{userId}")]
        public async Task<List<OrderInfo>> GetOrdersByUserId(int userId)
        {
            _logger.LogInformation($"Getting orders ");
            

            var orders = await _orderClient.GetOrdersByUserId(userId);
            return orders.Orders.Select(x => new OrderInfo(x)).ToList();

        }


        [HttpGet("{userId}")]
        public async Task<List<OrderInfo>> GetOrdersToPay(int userId)
        {
            _logger.LogInformation($"Getting orders ");
 
            var orders = await _orderClient.GetOrdersToPay(userId);
            return orders.Orders.Select(x => new OrderInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateOrder(OrderInfo orderInfo)
        {
            Console.WriteLine($"Order with made with userId: {orderInfo.UserId}");
            
            return await _orderClient.CreateOrder(orderInfo.Convert());

        }


         [HttpPost]
        public async Task<GeneralResponse> FinishOrder(FinishOrder finishOrder)
        {
            Console.WriteLine($"Order with Id {finishOrder.OrderId}has finished with reason: {finishOrder.Reason}");
            
            
            return await _orderClient.HandleLocalAvailabilityResponse(new LocalResponse{OrderId = finishOrder.OrderId, Reason = finishOrder.Reason});

        }

         [HttpPatch]
        public async Task<GeneralResponse> UpdateOrder(OrderInfo orderInfo)
        {
            Console.WriteLine($"Order with Id: {orderInfo.Id} updating...");
            
            return await _orderClient.UpdateOrder(orderInfo.Convert());

        }


         [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteOrderById(int Id)
        {
            Console.WriteLine($"Order with Id: {Id} deleting...");
            Order order = await _orderClient.GetOrderById(Id);
            return await _orderClient.DeleteOrder(order);

        }
    }


}
