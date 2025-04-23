using System.Net.Http;
using System.Threading.Tasks;
using Grpc.Net.Client;

namespace acmeat.server.order.client
{

    public class OrderClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcOrder.GrpcOrderClient _client;


        public OrderClient(


            )
        {
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress("http://ordermanager-service:8080", new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcOrder.GrpcOrderClient(_channel);

        }
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
    }
}