using System;
using System.Net.Http;
using System.Threading.Tasks;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Telerik.JustMock;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.order.client
{

    public class Payment{

    public Payment(
        string IBAN,
        string Causal,
        int Price,
        int TransactionId
    ){
        this.IBAN = IBAN;
        this.Causal =Causal;
        this.Price = Price;
        this.TransactionId = TransactionId;
    }



    public string IBAN {get;set;}
    public string Causal {get;set;}
    public int Price {get;set;}
    public int TransactionId{get;set;}
}

    public class OrderClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcOrder.GrpcOrderClient _client;
        private readonly OrderClientOptions _options;

        private static HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("https://jsonplaceholder.typicode.com"),
        };
         public interface ITaskAsync
    {
        Task<int> AsyncExecute(string value);
    }


        public OrderClient(IOptions<OrderClientOptions> options


            )
        {
            _options = options.Value;
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress(_options.OrderManagerConnectionString, new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcOrder.GrpcOrderClient(_channel);

        }

        #region  Order
        public async Task<Order> GetOrderById(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetOrderByIdAsync(id1);
        }

        public async Task<OrderList> GetOrderList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetOrdersAsync(id1);
        }

        public async Task<OrderList> GetOrdersByUserId(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetOrdersByUserIdAsync(id1);
        }

        public async Task<OrderList> GetOrdersToPay(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetOrdersToPayAsync(id1);
        }

        public async Task<GeneralResponse> CreateOrder(Order order)
        {


            return await _client.CreateOrderAsync(order);
        }

        public async Task<GeneralResponse> UpdateOrder(Order order)
        {

            return await _client.UpdateOrderAsync(order);
        }

        public async Task<GeneralResponse> DeleteOrder(Order order)
        {
            return await _client.DeleteOrderAsync(order);
        }
        #endregion


        #pragma warning disable CS0436 // Type conflicts with imported type
        #region  Bank
        public async Task<Payment> GetPaymentInfo(string Token)
        {
            Console.WriteLine($"Token received: {Token}. Getting Payment info");

            // TO DO REMOVE WHEN BANK ENDPOINT IS READY THE REQUEST MUST BE DONE BY THE BANK CLIENT
            var mock = Mock.Create<ITaskAsync>();
            Mock.Arrange(() => mock.AsyncExecute(Token));
            await mock.AsyncExecute(Token);

            return new Payment("AHAHAHAHHA", "BIGLIETTO", 30, 2);
        }

        #endregion
    }
}