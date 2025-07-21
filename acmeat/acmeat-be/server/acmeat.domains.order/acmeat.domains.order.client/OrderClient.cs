using System;
using System.Net.Http;
using System.Threading.Tasks;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.order.client
{


    public class OrderClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcOrder.GrpcOrderClient _client;
        private readonly OrderClientOptions _options;


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


        public async Task<GeneralResponse> HandleLocalAvailabilityResponse(LocalResponse localResponse)
        {
            return await _client.HandleLocalAvailabilityResponseAsync(localResponse);
        }
        #endregion


#pragma warning disable CS0436 // Type conflicts with imported type
        #region  Bank

        public async Task<GeneralResponse> VerifyPayment(Payment payment)
        {
            return await _client.VerifyPaymentAsync(payment);
        }

        #endregion
    }
}