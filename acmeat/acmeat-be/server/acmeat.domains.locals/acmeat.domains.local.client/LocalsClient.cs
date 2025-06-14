using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

using acmeat.db.order;
using acmeat.server.order.client;

using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.local.client
{

    //    public class DishInfo
    // {
    //     public int Id { get; set; }
    //     public int Quantity { get; set; }

    //     public DishInfo()
    //     {

    //     }


    //     public DishInfo(int Id, int Quantity)
    //     {

    //         this.Id = Id;
    //         this.Quantity = Quantity;

    //     }



    // }


    public class LocalClient
    {

        private readonly static HttpClient _sharedClient = new HttpClient()
        {
            BaseAddress = new Uri("https://braciebasilico.romanellas.cloud")
        };
        private const string protocol = "https://";

        private Dictionary<string, string> map = new Dictionary<string, string>()
        {
            {"braciebasilico.romanellas.cloud","80f22804dd14e07889619ec9b1309bc9cd3ca6f9cfcaee3f17d4e8bdbd954197"},
            {"osteriamareebosco.romanellas.cloud","a0e29ffd3c729ba0c2aafce4c7d9de96d6d63184b4429d9794af66b43a813d04"},
            {"ilvicolettosegreto.romanellas.cloud","187f762581ae8eebf48e78c33c645c42009eef76441d3b5c328285f337db211c"},
            {"laforchettaribelle.romanellas.cloud","bc94bbdf31f3f69617cae08a985b08bd8d8d094264cf7a7f83167cafc40455a5"},
            {"cantinafiordisale.romanellaas.cloud","0ad7c62f635bcbb964f97540add5522d599c3af9fa603916245bc208571bd09e"},
        };
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcLocal.GrpcLocalClient _client;
        private readonly LocalClientOptions _options;


        public LocalClient(IOptions<LocalClientOptions> options


            )
        {
            _options = options.Value;
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress(_options.LocalManagerConnectionString, new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcLocal.GrpcLocalClient(_channel);

        }
        public async Task<Local> GetLocalById(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetLocalByIdAsync(id1);
        }

        public async Task<Local> GetLocalByUrl(string url)
        {
            client.Url url1 = new client.Url();
            url1.Url_ = url;
            return await _client.GetLocalByUrlAsync(url1);
        }

        public async Task<LocalList> GetLocalList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetLocalsAsync(id1);
        }

        public async Task<LocalList> GetLocalListByCity(string city)
        {
            City city1 = new City();
            city1.City_ = city;
            return await _client.GetLocalsByCityAsync(city1);
        }


        public async Task<GeneralResponse> CreateLocal(Local local)
        {


            return await _client.CreateLocalAsync(local);
        }

        public async Task<GeneralResponse> UpdateLocal(Local local)
        {

            return await _client.UpdateLocalAsync(local);
        }

        public async Task<GeneralResponse> DeleteLocal(Local local)
        {
            return await _client.DeleteLocalAsync(local);
        }

        //check if the order can be placed at specific  local
        public async Task<GeneralResponse> CheckOrderAvailability(Order order, List<DishInfo> dishes, string localUrl)
        {
            Console.WriteLine($"Order received: {JsonConvert.SerializeObject(order)} sending to local with: {localUrl}");
            string? localToken = map.GetValueOrDefault(localUrl);

            if (localToken != null)
            {

                _sharedClient.DefaultRequestHeaders.Add("Authorization", localToken);
            }
            else
            {
                throw new Exception($"Local url{localUrl} is not defined in the allowed ones");
            }


            var jsonResponse = await _sharedClient.PostAsJsonAsync(new Uri(protocol + localUrl + "/api/order"),

            // TO DO: SEND THE REQUEST TO LOCALS
            new
            {
                dishes,
                deliveryTime = order.DeliveryTime,
                dishIds = dishes.Select(dish => dish.Id)

            }
            );
            Console.WriteLine($"{jsonResponse}");

            GeneralResponse resposne = new GeneralResponse();
            resposne.Message = "OK";
            return resposne;
        }
        
        public async Task<GeneralResponse> CommunicateOrderCancellation(int orderId, string localUrl)
        {
            Console.WriteLine($"Order cacellation: {orderId}");
            string? localToken = map.GetValueOrDefault(localUrl);

            if (localToken != null)
            {

                _sharedClient.DefaultRequestHeaders.Add("Authorization", localToken);
            }
            else
            {
                throw new Exception($"Local url{localUrl} is not defined in the allowed ones");
            }
           

            var jsonResponse = await _sharedClient.DeleteAsync(new Uri(protocol + localUrl + "/api/order/"+orderId)

            );
            Console.WriteLine($"{jsonResponse}");
            
            GeneralResponse resposne = new GeneralResponse();
            resposne.Message = "OK";
            return resposne;
        }
    }
}