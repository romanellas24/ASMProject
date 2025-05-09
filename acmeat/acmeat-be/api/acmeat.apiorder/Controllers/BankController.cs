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
            BaseAddress = new Uri("https://jsonplaceholder.typicode.com"),
        };


         public BankController(
            OrderClient orderClient,
            ILogger<BankController> logger

         ){

            _logger = logger;
            _orderClient = orderClient;
            
        }

        //TO DO INSERT FAKEBANK URL
       
        [HttpPut]
        public async Task<string> Pay(PaymentInfo paymentInfo)
        {
            Console.WriteLine($"received payment Info  {System.Text.Json.JsonSerializer.Serialize(paymentInfo)}");

            //TO DO INSERT THE BANK ENDOPOINT
            await sharedClient.PutAsJsonAsync("/Pay", paymentInfo);

            return GenerateJwtToken(paymentInfo.IBAN);

        }




        [HttpGet("{Token}")]
        public async Task<PaymentInfo> GetPaymentInfo(string Token)
        {
            Console.WriteLine($"Token received: {Token}. Getting Payment info");
            

            return  new PaymentInfo(await _orderClient.GetPaymentInfo(Token));

            // TO DO REMOVE WHEN BANK ENDPOINT IS READY THE REQUEST MUST BE DONE BY THE BANK CLIENT
          
        }

        [HttpDelete("{Token}")]
        public async Task<HttpResponseMessage> DeleteTransaction(string Token){
            Console.WriteLine($"token received: {Token}. Deleting transaction");
            var mock = Mock.Create<ITaskAsync>();
            Mock.Arrange(() => mock.AsyncExecute(Token));
            await mock.AsyncExecute(Token);

            return new HttpResponseMessage(System.Net.HttpStatusCode.OK);
        }
        private string GenerateJwtToken(string username)
        {
            var claims = new[]
            {
            new Claim(JwtRegisteredClaimNames.Sub, username),
            new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
        };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("acmeat/acmeat-be/api/key/keyfile"));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: "yourdomain.com",
                audience: "yourdomain.com",
                claims: claims,
                expires: DateTime.Now.AddMinutes(30),
                signingCredentials: creds);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
