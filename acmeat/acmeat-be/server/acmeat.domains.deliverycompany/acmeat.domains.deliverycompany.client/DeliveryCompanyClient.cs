using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Telerik.JustMock;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.deliverycompany.client
{

    public class DeliveryCompanyClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcDeliveryCompany.GrpcDeliveryCompanyClient _client;
        private readonly DeliveryCompanyClientOptions _options;


        // API's for azure maps
        private readonly string AZURE_MAPS_URL = "https://atlas.microsoft.com/geocode?api-version=2025-01-01&addressLine=";

        private readonly string XMSCLIENTID = "46fad49a-d535-4e30-84ed-21e2139c70ec";
        private readonly string SUBSCRIPTIONKEY = "BBUY6Nr3vvNNyTb79dK3LVXPVNmHlviTlm8qE1MfX2suFFsWPNoSJQQJ99BFACYeBjFnlIanAAAgAZMP3cFB";

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

            //REQUEST LOCATION VIA AZURE MAPS
            _sharedClient.DefaultRequestHeaders.Add("x-ms-client-id", XMSCLIENTID);
            _sharedClient.DefaultRequestHeaders.Add("subscription-key", SUBSCRIPTIONKEY);

            Location? userLocation = await _sharedClient.GetFromJsonAsync<Location>(new Uri(AZURE_MAPS_URL + availabilityPayload.UserAddress));

            Location? Locallocation = await _sharedClient.GetFromJsonAsync<Location>(new Uri(AZURE_MAPS_URL + availabilityPayload.LocalAddress));


            //ASKING DELIVERY COMPANY FOR AVAILABILITY
            //FLUSH HEADERS
            _sharedClient.DefaultRequestHeaders.Clear();

            Uri uriString = new Uri(protocol + availabilityPayload.DeliveryCompanyUrl
             + "/allocation/restAddr=%7B%22lat%22%3A" + userLocation?.features.First().geometry.coordinates[1]
             + "%2C%22lng%22%3A" + userLocation?.features.First().geometry.coordinates[0]
             + "%7D&clientAddr=%7B%22lat%22%3A" + Locallocation?.features.First().geometry.coordinates[1]
             + "%2C%22lng%22%3A" + Locallocation?.features.First().geometry.coordinates[0]
             + "%7D&expectedDeliveryTime=" + availabilityPayload.DeliveryTime);
            DeliveryCompanyAvailabilityResponse? deliveryCompanyAvailabilityResponse =
             await _sharedClient.GetFromJsonAsync<DeliveryCompanyAvailabilityResponse>(uriString);


            if (deliveryCompanyAvailabilityResponse != null)
            {
                if (deliveryCompanyAvailabilityResponse.isVehicleAvailable == true)
                {

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

        // TO DO CHANGE WITH THE ENDPOINT
        public async Task<GeneralResponse> CommunicateOrderToDeliveryCompany(DeliveryCompanyAvailabilityResponse deliveryCompanyAvailabilityResponse, AvailabilityPayload availabilityPayload)
        {

            GeneralResponse generalResponse = new GeneralResponse();
           HttpResponseMessage? deliveryCompanyResponse= await _sharedClient.PutAsJsonAsync(protocol + availabilityPayload.DeliveryCompanyUrl + "/allocation",
            new DeliveryCompanyOrderPlacement
            {
                vehicle = deliveryCompanyAvailabilityResponse.vehicleId,
                timeMinutes = deliveryCompanyAvailabilityResponse.time,
                expectedDeliveryTime = availabilityPayload.DeliveryTime,
                companyName = "acmeat",
                hash ="a1b2c3d4e5f6"
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
        

         public async Task<GeneralResponse> SendOrderCancellationToDeliveryCompany(AvailabilityPayload availabilityPayload)
        {
            var mock = Mock.Create<ITaskAsync>();
                    Mock.Arrange(() => mock.AsyncExecute(availabilityPayload));
                    await mock.AsyncExecute(availabilityPayload);

            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.Message = "OK";
            return generalResponse;
        }
    }
}