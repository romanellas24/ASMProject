
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

        [HttpPost]
        public async Task<GeneralResponse> CreateOrder(OrderInfo orderInfo)
        {
            Console.WriteLine($"Order with made with userId: {orderInfo.UserId}");
            
            OrderClient client = new OrderClient();
            return await client.CreateOrder(orderInfo.Convert());

        }
    }


}
