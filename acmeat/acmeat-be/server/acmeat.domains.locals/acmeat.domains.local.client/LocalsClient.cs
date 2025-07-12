using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Threading;
using System.Threading.Tasks;
using Zeebe.Client;
using acmeat.db.order;
using acmeat.server.order.client;
using Azure.Core;
using Grpc.Core;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using Azure;
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
            {"braciebasilico.romanellas.cloud","468965f7f8b123e992f92d60c77a8866b9196ebcc769e9223f5705f550784487"},
            {"osteriamareebosco.romanellas.cloud","522726a3c54729c462fb20e2fd83c271c12d33dc0eb3ceb75919aed1c4a8c209"},
            {"ilvicolettosegreto.romanellas.cloud","07dab70e031d47001f7ebc0cee7759e0c1e7aa21e2e9f52ecae485ab29ddd599"},
            {"laforchettaribelle.romanellas.cloud","99bb7268a02d722247f955d0b7abb0d889b00b292861599c33241cfba069e769"},
            {"cantinafiordisale.romanellaas.cloud","3bd31909ecbf4ebd6f974d45a921f91dcdc893848e8012c5a6c35d4cbdf1ffb6"},
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

        //check if the order can be placed at specific  local 
        //IMPORTANT -> REQUEST COULD GO TIMEOUT 10S IN DEV AND 180S WHEN DEPLOYED
        public async Task<GeneralResponse> CheckOrderAvailability(string deliveryTime, List<DishInfo> dishes, string localUrl)
        {
            Console.WriteLine($"Checking Availability to local with: {localUrl}");
            await AuthenticateToLocal(localUrl);

            var time = TimeSpan.Parse(deliveryTime);



            HttpResponseMessage? jsonResponse = await _sharedClient.PostAsJsonAsync(new Uri(protocol + localUrl + "/api/order"),

            // TO DO: SEND THE REQUEST TO LOCALS
            new
            {
                dishes,
                deliveryTime = DateTime.Today.Add(time).ToUniversalTime().ToString("yyyy-MM-dd HH:mm"),
                companyName = "acmeat"

            }
            );

            GeneralResponse resposne = new GeneralResponse();
            if (jsonResponse != null && jsonResponse.StatusCode == HttpStatusCode.OK)
            {

                resposne.Message = "OK";

            }
            else
            {
                resposne.Message = $"Something went wrong checking awailability: {jsonResponse}";
            }

            return resposne;



        }



        //check if the order can be placed at specific  local 
        //IMPORTANT -> REQUEST COULD GO TIMEOUT 10S IN DEV AND 180S WHEN DEPLOYED
        public async Task<GeneralResponse> CheckOrderAvailabilityServiceWorkerAsync(string deliveryTime, List<DishInfo> dishes, string localUrl, ZeebeClient zeebeClient)
        {
            Console.WriteLine($"Checking Availability to local with: {localUrl}");
            GeneralResponse resposne = new GeneralResponse();
            
             var tcs = new TaskCompletionSource<GeneralResponse>();

            var tcs2 = new TaskCompletionSource<GeneralResponse>();


             var NotifyOrderNotPlacedWorker = zeebeClient
               .NewWorker()
               .JobType("NotifyOrderNotPlaced")
               .Handler(async (jobClient, job) =>
               {
                   var localResponse = new GeneralResponse();

                   try
                   {

                       await jobClient
                           .NewCompleteJobCommand(job.Key)
                           .Send();

                       localResponse.Message = "Order canceled. You can order only between 12-14 and 19-22";
                       tcs2.TrySetResult(localResponse);
                   }
                   catch (Exception ex)
                   {
                       localResponse.Message = $"Exception in handler: {ex.Message}";
                       tcs2.TrySetException(ex); // opzionale se vuoi catchare nel codice principale
                   }

                  
               })
               .MaxJobsActive(1)
               .Name(Environment.MachineName)
               .PollInterval(TimeSpan.FromSeconds(1))
               .Timeout(TimeSpan.FromSeconds(10))
               .Open();


             



var worker = zeebeClient
       .NewWorker()
       .JobType("CheckLocalAvailabilty")
       .Handler( async(jobClient, job) =>
       {
           // TO DECOMMENT

           //  AuthenticateToLocal(localUrl).GetAwaiter().GetResult();
                string jsonStrng =job.Type;
           var time = TimeSpan.Parse(deliveryTime);
           var localResponse = new GeneralResponse();

           


           //TO DECOMMENT
           //  HttpResponseMessage? jsonResponse = _sharedClient.PostAsJsonAsync(new Uri(protocol + localUrl + "/api/order"),

           //  // TO DO: SEND THE REQUEST TO LOCALS
           //  new
           //  {
           //      dishes,
           //      deliveryTime = DateTime.Today.Add(time).ToUniversalTime().ToString("yyyy-MM-dd HH:mm"),
           //      companyName = "acmeat"

           //  }
           //  ).GetAwaiter().GetResult();

           //TO DECOMMENT
           //  if (jsonResponse != null && jsonResponse.StatusCode == HttpStatusCode.OK)
           //  {

           localResponse.Message = "OK";
           await jobClient
            .NewCompleteJobCommand(job.Key)
            .Send()
        ;
           tcs.TrySetResult(localResponse);

           // TO DECOMMENT
           //  }
           //  else
           //  {
           // resposne.Message = $"Something went wrong checking awailability: {jsonResponse}";
                // FOR EVERY RESPONSE DIFFERENT FROM OK
                //            jobClient.NewThrowErrorCommand(job.Key)
           //   .ErrorCode("500")
           //   .ErrorMessage($"{resposne.Message}")
           //   .Send()
           //   .GetAwaiter()
           //   .GetResult();
           //  }

       })
       .MaxJobsActive(1)
       .Name(Environment.MachineName)
       .PollInterval(TimeSpan.FromSeconds(1))
       .Timeout(TimeSpan.FromSeconds(10))
       .Open();

            // signal.WaitOne(10000);


            // }
            // ;




            //IF BUSINESS RULES ARE BROKEN DON'T CREATE THE ORDER

          var  taskCompleted = await Task.WhenAny(tcs.Task, tcs2.Task);
            resposne = await taskCompleted;




                return resposne;
            

            




        }




        

        public async Task<GeneralResponse> CommunicateOrderCancellation(int orderId, string localUrl)
        {
            Console.WriteLine($"Order cacellation: {orderId}");
            await AuthenticateToLocal(localUrl);


            var jsonResponse = await _sharedClient.DeleteAsync(new Uri(protocol + localUrl + "/api/order/" + orderId)

            );

            GeneralResponse resposne = new GeneralResponse();
            if (jsonResponse != null && jsonResponse.StatusCode == HttpStatusCode.OK)
            {

                resposne.Message = "OK";

            }
            else
            {
                resposne.Message = $"Something went wrong cancelling order: {jsonResponse}";
            }

            return resposne;
        }
    }
}