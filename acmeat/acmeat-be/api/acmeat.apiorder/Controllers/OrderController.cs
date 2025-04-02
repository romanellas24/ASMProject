
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
            //TO DO: MAKE REQUEST TO FAKE BANK
            using HttpResponseMessage response = await sharedClient.GetAsync("paymentOrder/" + OrderId);
            return response;

        }


        [HttpGet("{Id}")]
        public async Task<OrderInfo> GetOrderById(int Id)
        {
            _logger.LogInformation($"Getting order with id: {Id}");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var order = await _orderClient.GetOrderById(Id);
            return new OrderInfo(order);

        }

        [HttpGet]
        public async Task<List<OrderInfo>> GetOrders()
        {
            _logger.LogInformation($"Getting orders ");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var orders = await _orderClient.GetOrderList();
            return orders.Orders.Select(x => new OrderInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateOrder(OrderInfo orderInfo)
        {
            Console.WriteLine($"Order with made with userId: {orderInfo.UserId}");
            
            return await _orderClient.CreateOrder(orderInfo.Convert());

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
            
            return await _orderClient.DeleteOrder( new Order{Id=Id});

        }
    }


}
