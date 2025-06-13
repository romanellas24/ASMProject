using System;
using System.Net.Http;
using System.Security.Policy;
using System.Threading.Tasks;
using acmeat.db.order;
using acmeat.server.order.client;
using Azure;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
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
        public GeneralResponse CheckOrderAvailability(Order order,int localId){
           Console.WriteLine($"Order received: {JsonConvert.SerializeObject(order)} sending to local with: {localId}");

           // TO DO: SEND THE REQUEST TO LOCALS
           GeneralResponse resposne = new GeneralResponse();
            resposne.Message="OK";
          return resposne;
        }
    }
}