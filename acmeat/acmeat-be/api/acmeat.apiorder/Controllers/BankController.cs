using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using acmeat.server.order.client;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore.Storage;
using Microsoft.IdentityModel.Tokens;
using Telerik.JustMock;

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
        public async Task<GeneralResponse> VerifyPayment(string  transactionId,int orderId)
        {
            Console.WriteLine($"received payment Info for order with id {orderId}");
            GeneralResponse generalResponse = new GeneralResponse();
            //TO DO INSERT THE BANK ENDOPOINT
            BankPaymentVerification? bankPaymentVerification = await sharedClient.GetFromJsonAsync<BankPaymentVerification>(sharedClient.BaseAddress + "payments/"+ transactionId);
            if (bankPaymentVerification != null && bankPaymentVerification.status == "Paid")
            {
                _logger.LogInformation($"Payment confirmed, updating order {orderId}");

                //UPDATE
                Order orderToUpdate = await _orderClient.GetOrderById(orderId);
                orderToUpdate.PurchaseTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
                orderToUpdate.TransactionId = transactionId;

                generalResponse = await _orderClient.UpdateOrder(orderToUpdate);
                _logger.LogInformation($"order update : {generalResponse.Message}");
                return generalResponse;
            }
            else
            {
                generalResponse.Message = "an error occured during token verification";
                return generalResponse;
            }

        }



        [HttpDelete("{transactionId}")]
        public async Task<GeneralResponse> DeleteTransaction(string transactionId){
            Console.WriteLine($"token received: {transactionId}. Deleting transaction");

            GeneralResponse generalResponse = new GeneralResponse();

            HttpResponseMessage? bankTransctiondeletion = await sharedClient.DeleteAsync(sharedClient.BaseAddress + "payments/"+ transactionId);
            if (bankTransctiondeletion != null && bankTransctiondeletion.StatusCode == System.Net.HttpStatusCode.OK)
            {
                
                _logger.LogInformation($"refound started");
                generalResponse.Message = "OK";
                return generalResponse;
            }
            else
            {
                generalResponse.Message = $"an error occured during transaction deletion code:{bankTransctiondeletion?.StatusCode} content: {bankTransctiondeletion?.Content}";
                return generalResponse;
            }
        }
    }
}
