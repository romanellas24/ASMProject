
using acmeat.server.order.client;
using Microsoft.AspNetCore.Mvc;


namespace acmeat.api.order
{
    //TO DO REMOVE WHEN FINISHED MOCKING
    public interface ITaskAsync
    {
        Task<int> AsyncExecute(string value);
    }


    [Route("api/[controller]/[action]")]
    [ApiController]
    public class BankController : ControllerBase
    {


      
        private readonly OrderClient _orderClient;
        private readonly ILogger<BankController> _logger;

        //TO DO INSERT FAKEBANK URL
        private static HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("https://joliebank.romanellas.cloud/"),
        };


         public BankController(
            OrderClient orderClient,
            ILogger<BankController> logger

         ){

            _logger = logger;
            _orderClient = orderClient;
            
        }

        [HttpPatch]
        public async Task<GeneralResponse> VerifyPayment(string transactionId, int orderId)
        {
            Console.WriteLine($"received payment Info for order with id {orderId}");
            // GeneralResponse generalResponse = new GeneralResponse();
            //TO DO INSERT THE BANK ENDOPOINT
            return await _orderClient.VerifyPayment(new Payment
            {
                TransactionId = transactionId,
                OrderId=orderId
            });
          
        }



      
    }
}
