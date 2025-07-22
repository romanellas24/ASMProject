using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Threading;
using System.Threading.Tasks;

using acmeat.server.order.client;

using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;

//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.local.client
{


    public class AuthLocal
    {
        public AuthLocal() { }
        public required string username { get; set; }
        public required string password { get; set; }
    }

    public class AuthToken
    {
        [JsonConstructor]
        public AuthToken(string jwt)
        {

            this.jwt = jwt;
        }
        [JsonProperty("jwt")]
        public required string jwt { get; set; }
    }
    public class LocalClient
    {

        private readonly static HttpClient _sharedClient = new HttpClient()
        {
            BaseAddress = new Uri("https://braciebasilico.romanellas.cloud")
        };
        private const string protocol = "https://";

        private Dictionary<string, string> map = new Dictionary<string, string>()
        {
            {"braciebasilico.romanellas.cloud","faada5c4c44618391ba87f576b9cda71226df61c491d2ee2698404fb40ab468e"},
            {"osteriamareebosco.romanellas.cloud","3f30792c3a91198e2952d44abb2106875f70ab2f8e859f643ab693b03c5ba124"},
            {"ilvicolettosegreto.romanellas.cloud","80ea206cec6c038a4bacc2797c798da8cf57a1b41de51fc3bbccbf56026e7cdc"},
            {"laforchettaribelle.romanellas.cloud","678b022f5e3b77d2186051260ee99350f590f8055de6de1f32baf4c4289367df"},
            {"cantinafiordisale.romanellas.cloud","7785e546b41261118f29138b548dcc5630ba7108c939bb884836a44a7d699307"},
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

        public async Task AuthenticateToLocal(string localUrl)
        {
            Console.WriteLine($"Authenticating to {localUrl}");

            string? localToken = map.GetValueOrDefault(localUrl);

            if (localToken != null)
            {
                AuthLocal authLocal = new AuthLocal()
                {
                    username = "acmeat",
                    password = localToken
                };
                HttpResponseMessage? jsonResponse = await _sharedClient.PostAsJsonAsync(new Uri(protocol + localUrl + "/auth/login"), authLocal);

                if (jsonResponse != null && jsonResponse.StatusCode == HttpStatusCode.OK)
                {
                    AuthToken? authToken = await jsonResponse.Content.ReadFromJsonAsync<AuthToken>();

                    //flush headers
                    _sharedClient.DefaultRequestHeaders.Clear();
                    _sharedClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", authToken?.jwt);

                }
                else
                {
                    throw new Exception($"Response from {localUrl} wasn't successfull {jsonResponse?.StatusCode} : {jsonResponse?.Content.ToString()}");
                }
            }
            else
            {
                throw new Exception($"Local with url {localUrl} doesn't exist");
            }



        }

     
        public async Task<GeneralResponse> CheckOrderAvailability(int orderId,string deliveryTime, List<DishInfo> dishes, string localUrl)
        {
            Console.WriteLine($"Checking Availability to local with: {localUrl}");
            await AuthenticateToLocal(localUrl);

  



            HttpResponseMessage? jsonResponse = await _sharedClient.PostAsJsonAsync(new Uri(protocol + localUrl + "/api/order"),

            // TO DO: SEND THE REQUEST TO LOCALS
            new
            {
                dishes,
                deliveryTime = deliveryTime,
                id=orderId,
                companyName = "acmeat"
                

            }
            );

            GeneralResponse resposne = new GeneralResponse();
            if (jsonResponse != null && jsonResponse.StatusCode == HttpStatusCode.Accepted)
            {

                resposne.Message = "OK";

            }
            else
            {
                resposne.Message = $"Something went wrong checking awailability: {jsonResponse}";
            }

            return resposne;



        }


          public async Task<GeneralResponse> CommunicateOrderCancellation(int orderId, string localUrl)
        {
            Console.WriteLine($"Deleting Order to local with: {localUrl}");
            await AuthenticateToLocal(localUrl);

  



            HttpResponseMessage? jsonResponse = await _sharedClient.DeleteAsync(new Uri(protocol + localUrl + "/api/order/" + orderId + "?company=acmeat")

            );

            GeneralResponse resposne = new GeneralResponse();
            if (jsonResponse != null && jsonResponse.StatusCode == HttpStatusCode.Accepted)
            {

                resposne.Message = "OK";

            }
            else
            {
                resposne.Message = $"Something went wrong with deleting the order: {jsonResponse}";
            }

            return resposne;



        }





        public async Task<GeneralResponse> catchTimeout()
        {
            await Task.Delay(10000);
            return new GeneralResponse { Message = "TIMEOUT" };
        }
    }


}