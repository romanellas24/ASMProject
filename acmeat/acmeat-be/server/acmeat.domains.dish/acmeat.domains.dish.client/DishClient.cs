using System.Net.Http;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.dish.client
{

    public class DishClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcDish.GrpcDishClient _client;
        private readonly DishClientOptions _options;


        public DishClient(IOptions<DishClientOptions>  options


            )
        {
            _options = options.Value;
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress(_options.DishManagerConnectionString, new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcDish.GrpcDishClient(_channel);

        }
        public async Task<Dish> GetDishById(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetDishByIdAsync(id1);
        }

        public async Task<DishList> GetDishsByMenuId(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetDishsByMenuIdAsync(id1);
        }

        public async Task<DishList> GetDishList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetDishsAsync(id1);
        }

        public async Task<GeneralResponse> CreateDish(Dish dish)
        {


            return await _client.CreateDishAsync(dish);
        }

        public async Task<GeneralResponse> UpdateDish(Dish dish)
        {

            return await _client.UpdateDishAsync(dish);
        }

          public async Task<GeneralResponse> UpdateDishs(DishList dishs)
        {
            return await _client.UpdateDishsAsync(dishs);     
        }

        public async Task<GeneralResponse> DeleteDish(Dish dish)
        {
            return await _client.DeleteDishAsync(dish);
        }
    }
}