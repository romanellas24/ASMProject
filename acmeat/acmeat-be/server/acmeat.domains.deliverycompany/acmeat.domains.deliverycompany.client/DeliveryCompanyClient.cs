using System.Net.Http;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.deliverycompany.client
{

    public class DeliveryCompanyClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcDeliveryCompany.GrpcDeliveryCompanyClient _client;
        private readonly DeliveryCompanyClientOptions _options;


        public DeliveryCompanyClient(IOptions<DeliveryCompanyClientOptions>  options


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
    }
}