using System.Threading.Tasks;
using acmeat.server.local.manager;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.local.dataproxy;
using acmeat.server.local.datawriter;
using acmeat.server.local;
using System.Collections.Generic;
using Google.Protobuf;
using System.Linq;
using System;

namespace acmeat.domains.local.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcLocalManagerService : server.local.manager.GrpcLocal.GrpcLocalBase
{
    private readonly ILogger<GrpcLocalManagerService> _logger;
    private LocalReader _localReader;
    private  LocalDataWriter _localDataWriter;
    public GrpcLocalManagerService(
        ILogger<GrpcLocalManagerService> logger,
        LocalReader localReader,
        LocalDataWriter localDataWriter)
    {
        _logger = logger;
        _localDataWriter = localDataWriter;
        _localReader = localReader;
    }

    public override Task<server.local.manager.HelloReplyClient> SayHello(server.local.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.local.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.local.manager.Local> GetLocalById(Id id, ServerCallContext context)
    {
        return Task.FromResult(
            
           new server.local.manager.Local(ConvertServerModelToGrpc(_localReader.GetLocalById(id.Id_)))
        );
    }

     public override Task<server.local.manager.LocalList> GetLocals(Id id, ServerCallContext context)
    {
        List<server.local.Local> locals=  _localReader.GetLocals();
        LocalList localList =  new server.local.manager.LocalList();

        localList.Locals.AddRange(ConvertServerListToGrpc(locals));

        return Task.FromResult(
            localList
          
        );
    }

       public override async Task<GeneralResponse> CreateLocal(server.local.manager.Local local, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _localDataWriter.SendAsync(
            new CreateNewLocalCommand(
                ConvertGrpcToServerModel(local)
                )
            );
            generalResponse.Message="OK";

        }catch(Exception ex){
            generalResponse.Message = ex.Message;
        }
        return await Task.FromResult(
            generalResponse
        );

   }

       public override async Task<GeneralResponse> UpdateLocal(server.local.manager.Local local, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _localDataWriter.SendAsync(
            new UpdateNewLocalCommand(
                ConvertGrpcToServerModel(local)
                )
            );
            generalResponse.Message="OK";

        }catch(Exception ex){
            generalResponse.Message = ex.Message;
        }
        return await Task.FromResult(
            generalResponse
        );

   }

       public override async Task<GeneralResponse> DeleteLocal(server.local.manager.Local local, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _localDataWriter.SendAsync(
            new DeleteNewLocalCommand(
                local.Id
                )
            );
            generalResponse.Message="OK";

        }catch(Exception ex){
            generalResponse.Message = ex.Message;
        }
        return await Task.FromResult(
            generalResponse
        );

   }


    public List<server.local.manager.Local> ConvertServerListToGrpc(List<server.local.Local> locals){
        return locals.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.local.Local ConvertGrpcToServerModel(server.local.manager.Local local){
        return new server.local.Local(local.Id,local.Name,local.OpeningTime,local.ClosingTime,local.Address,local.OpeningDays,local.Available );
    }


    public server.local.manager.Local ConvertServerModelToGrpc(server.local.Local local){
        var localt =  new server.local.manager.Local();
        localt.Id = local.Id;
        localt.Name = local.Name;
        localt.OpeningDays = local.OpeningDays;
        localt.OpeningTime = local.OpeningTime;
        localt.ClosingTime = local.ClosingTime;
        localt.Address = local.Address;
        localt.Available = local.Available;
        return localt;

    }
}
