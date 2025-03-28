using System.Threading.Tasks;
using Grpc.Core;
using Microsoft.Extensions.Logging;

namespace acmeat.domains.order.Services;

public class GrpcOrderManagerService : server.order.manager.GrpcOrder.GrpcOrderBase
{
    private readonly ILogger<GrpcOrderManagerService> _logger;
    public GrpcOrderManagerService(ILogger<GrpcOrderManagerService> logger)
    {
        _logger = logger;
    }

    public override Task<server.order.manager.HelloReplyClient> SayHello(server.order.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.order.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }
}
