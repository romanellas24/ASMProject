using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;

//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.deliverycompany.client
{

    public class DeliveryCompanyClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcDeliveryCompany.GrpcDeliveryCompanyClient _client;
        private readonly DeliveryCompanyClientOptions _options;

        private readonly static HttpClient _sharedClient = new HttpClient()
        {
            BaseAddress = new Uri("https://braciebasilico.romanellas.cloud")
        };
        private const string protocol = "https://";

        // private Dictionary<string, string> map = new Dictionary<string, string>()
        // {
        //     {"cimangiamo.romanellas.cloud","468965f7f8b123e992f92d60c77a8866b9196ebcc769e9223f5705f550784487"},
        //     {"famechimica.romanellas.cloud","522726a3c54729c462fb20e2fd83c271c12d33dc0eb3ceb75919aed1c4a8c209"},
        //     {"panzafly.romanellas.cloud","07dab70e031d47001f7ebc0cee7759e0c1e7aa21e2e9f52ecae485ab29ddd599"},
        //     {"toctocgnam.romanellas.cloud","99bb7268a02d722247f955d0b7abb0d889b00b292861599c33241cfba069e769"}
        // };

        public class AvailabilityPayload
        {
            public AvailabilityPayload(
        string LocalAddress,
        string UserAddress,
        string DeliveryTime,
        string DeliveryCompanyUrl
    )
            {
                this.LocalAddress = LocalAddress;
                this.UserAddress = UserAddress;
                this.DeliveryTime = DeliveryTime;
                this.DeliveryCompanyUrl = DeliveryCompanyUrl;
            }



            public string LocalAddress { get; set; }
            public string UserAddress { get; set; }
            public string DeliveryTime { get; set; }
            public string DeliveryCompanyUrl { get; set; }

        }
        public interface ITaskAsync
        {
            Task<int> AsyncExecute(AvailabilityPayload value);
        }

        public DeliveryCompanyClient(IOptions<DeliveryCompanyClientOptions> options


            )
        {
            _options = options.Value;
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress(_options.DeliveryCompanyManagerConnectionString, new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcDeliveryCompany.GrpcDeliveryCompanyClient(_channel);

        }
        public async Task<DeliveryCompany> GetDeliveryCompanyById(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetDeliveryCompanyByIdAsync(id1);
        }

        public async Task<DeliveryCompanyList> GetDeliveryCompanyList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetDeliveryCompanysAsync(id1);
        }

        public async Task<GeneralResponse> CreateDeliveryCompany(DeliveryCompany deliverycompany)
        {
            return await _client.CreateDeliveryCompanyAsync(deliverycompany);
        }

        public async Task<GeneralResponse> UpdateDeliveryCompany(DeliveryCompany deliverycompany)
        {

            return await _client.UpdateDeliveryCompanyAsync(deliverycompany);
        }

        public async Task<GeneralResponse> DeleteDeliveryCompany(DeliveryCompany deliverycompany)
        {
            return await _client.DeleteDeliveryCompanyAsync(deliverycompany);
        }

        // TO DO CHANGE WITH THE ENDPOINT
        public async Task<GeneralResponse> CheckAvailability(AvailabilityPayload availabilityPayload)
        {

            GeneralResponse generalResponse = new GeneralResponse();
            Console.WriteLine($" Getting coordinates for User Address...");

             Uri uriString = new Uri(protocol + availabilityPayload.DeliveryCompanyUrl
             + "/api/allocation//availability-check?localAddress=" + availabilityPayload.LocalAddress
             + "&userAddress=" +availabilityPayload.UserAddress
             +"&deliveryTime=" + availabilityPayload.DeliveryTime );

            DeliveryCompanyAvailabilityResponse? deliveryCompanyAvailabilityResponse =
             await _sharedClient.GetFromJsonAsync<DeliveryCompanyAvailabilityResponse>(uriString);


            if (deliveryCompanyAvailabilityResponse != null)
            {



                if (deliveryCompanyAvailabilityResponse.isVehicleAvailable == true)
                {
                    generalResponse.Message = "OK";
                }
                else if (deliveryCompanyAvailabilityResponse.distance > 10)
                {
                    generalResponse.Message = "The delivery company is too distant from the user";   
                }
                else
                {
                    generalResponse.Message = "The delivery company has no veichles available";
                }
                

            }
            else
            {

                generalResponse.Message = "The delivery company is not available";
            }

            return generalResponse;
        }


        public async Task<DeliveryCompanyAvailabilityResponse2v> CheckAvailabilityWorker(AvailabilityPayload availabilityPayload)
        {

            Console.WriteLine($" Getting coordinates for User Address...");

            //REQUEST LOCATION VIA AZURE MAPS
            

            Uri uriString = new Uri(protocol + availabilityPayload.DeliveryCompanyUrl
             + "/api/allocation/availability-check?localAddress=" + availabilityPayload.LocalAddress
             + "&userAddress=" +availabilityPayload.UserAddress
             +"&deliveryTime=" + availabilityPayload.DeliveryTime );
            DeliveryCompanyAvailabilityResponse? deliveryCompanyAvailabilityResponse =
             await _sharedClient.GetFromJsonAsync<DeliveryCompanyAvailabilityResponse>(uriString);

            DeliveryCompanyAvailabilityResponse2v deliveryCompanyAvailabilityResponse2V = new DeliveryCompanyAvailabilityResponse2v();
            if (deliveryCompanyAvailabilityResponse != null)
            {
                deliveryCompanyAvailabilityResponse2V = new DeliveryCompanyAvailabilityResponse2v
                {
                    distance = deliveryCompanyAvailabilityResponse.distance,
                    isVehicleAvailable = deliveryCompanyAvailabilityResponse.isVehicleAvailable,
                    price = deliveryCompanyAvailabilityResponse.price,
                    time = deliveryCompanyAvailabilityResponse.time,
                    DeliveryCompanyUrl = availabilityPayload.DeliveryCompanyUrl,
                    vehicleId = deliveryCompanyAvailabilityResponse.vehicleId


             };
            }
            
            

            return deliveryCompanyAvailabilityResponse2V;
        }
        
        

        // TO DO CHANGE WITH THE ENDPOINT
        public async Task<GeneralResponse> CommunicateOrderToDeliveryCompany(int orderId,DeliveryCompanyAvailabilityResponse2v deliveryCompanyAvailabilityResponse, AvailabilityPayload availabilityPayload)
        {

            GeneralResponse generalResponse = new GeneralResponse();
            HttpResponseMessage? deliveryCompanyResponse = await _sharedClient.PutAsJsonAsync(protocol + availabilityPayload.DeliveryCompanyUrl + "/api/allocation",
             new DeliveryCompanyOrderPlacement
             {
                 orderId = orderId,
                 vehicle = deliveryCompanyAvailabilityResponse.vehicleId,
                 timeMinutes = deliveryCompanyAvailabilityResponse.time,
                 expectedDeliveryTime = availabilityPayload.DeliveryTime,
                 companyName = "acmeat",
                 hash = "6eed64279a4d6d745ab873466bcdcac28573acb423605d25d1b5b9ff95ce0b3b",
                 localAddress = availabilityPayload.LocalAddress,
                 userAddress= availabilityPayload.UserAddress
             }

             );

            if (deliveryCompanyResponse.StatusCode == HttpStatusCode.OK)
            {
                generalResponse.Message = "OK";
            }
            else
            {
                generalResponse.Message = $"An error occured in the assignment of the order to the delivery company. Status code : {deliveryCompanyResponse.StatusCode} content: {deliveryCompanyResponse.Content}";
            }

            return generalResponse;
        }
        

         public async Task<GeneralResponse> SendOrderCancellationToDeliveryCompany(int orderId,string DeliveryCompanyUrl)
        {
           GeneralResponse generalResponse = new GeneralResponse();
            var hash = new
            {
                hash = "6eed64279a4d6d745ab873466bcdcac28573acb423605d25d1b5b9ff95ce0b3b"
            };
            var request = new HttpRequestMessage(HttpMethod.Delete, protocol + DeliveryCompanyUrl + "/api/order/" + orderId+ "?company=acmeat" );
            request.Content = new StringContent(JsonConvert.SerializeObject(hash), Encoding.UTF8, "application/json");
           

            HttpResponseMessage? deliveryCompanyResponse =
             await _sharedClient.SendAsync(request);

            if (deliveryCompanyResponse.StatusCode == HttpStatusCode.OK)
            {
                generalResponse.Message = "OK";
            }
            else
            {
                generalResponse.Message = $"An error occured in the deletion of the order to the delivery company. Status code : {deliveryCompanyResponse.StatusCode} content: {deliveryCompanyResponse.Content}";
            }
            return generalResponse;
        }
    }
}