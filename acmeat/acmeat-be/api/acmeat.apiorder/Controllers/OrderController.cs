
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.order
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class OrderController : ControllerBase
    {
        //TO DO INSERT FAKEBANK URL
        private static HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("https://jsonplaceholder.typicode.com"),
        };

        [HttpGet("{OrderId}")]
        public async Task<HttpResponseMessage> GetPaymentForm(int OrderId)
        {
            Console.WriteLine($"order id: {OrderId}");
            //TO DO: MAKE REQUEST TO FAKE BANK
            using HttpResponseMessage response = await sharedClient.GetAsync("paymentOrder/"+OrderId);
            return response;

        }
    }
}
