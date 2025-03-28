using System;
using System.Net.Http;
using System.Threading.Tasks;
using Grpc.Net.Client;

#pragma warning disable CS0436 // Type conflicts with imported type
namespace acmeat.server.order
{



    class Program
    {

        public static async Task Main(string[] args)
        {
             var httpHandler = new HttpClientHandler();
    httpHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;
  
            using var channel = GrpcChannel.ForAddress("http://localhost:5201",new GrpcChannelOptions { HttpHandler = httpHandler });



            var client =new GrpcOrder.GrpcOrderClient(channel);

            HelloRequestClient request = new HelloRequestClient();
            request.Name = "prova";

            var response = await client.SayHelloAsync(request);


            Console.WriteLine(response.Message);
        }
    }
}

