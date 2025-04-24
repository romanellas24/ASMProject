using System.Net.Http;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.local.client
{

    public class LocalClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcLocal.GrpcLocalClient _client;
        private readonly LocalClientOptions _options;


        public LocalClient(IOptions<LocalClientOptions>  options


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

        public async Task<LocalList> GetLocalList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetLocalsAsync(id1);
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
    }
}