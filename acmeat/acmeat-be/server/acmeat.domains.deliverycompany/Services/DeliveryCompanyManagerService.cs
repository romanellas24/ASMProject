using System.Threading.Tasks;
using acmeat.server.deliverycompany.manager;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.deliverycompany.dataproxy;
using acmeat.server.deliverycompany.datawriter;
using acmeat.server.deliverycompany;
using System.Collections.Generic;
using Google.Protobuf;
using System.Linq;
using System;
using Microsoft.Azure.Cosmos.Linq;

namespace acmeat.domains.deliverycompany.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcDeliveryCompanyManagerService : server.deliverycompany.manager.GrpcDeliveryCompany.GrpcDeliveryCompanyBase
{
    private readonly ILogger<GrpcDeliveryCompanyManagerService> _logger;
    private DeliveryCompanyReader _deliverycompanyReader;
    private  DeliveryCompanyDataWriter _deliverycompanyDataWriter;
    public GrpcDeliveryCompanyManagerService(
        ILogger<GrpcDeliveryCompanyManagerService> logger,
        DeliveryCompanyReader deliverycompanyReader,
        DeliveryCompanyDataWriter deliverycompanyDataWriter)
    {
        _logger = logger;
        _deliverycompanyDataWriter = deliverycompanyDataWriter;
        _deliverycompanyReader = deliverycompanyReader;
    }

    public override Task<server.deliverycompany.manager.HelloReplyClient> SayHello(server.deliverycompany.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.deliverycompany.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.deliverycompany.manager.DeliveryCompany> GetDeliveryCompanyById(Id id, ServerCallContext context)
    {
        return Task.FromResult(
            
           new server.deliverycompany.manager.DeliveryCompany(ConvertServerModelToGrpc(_deliverycompanyReader.GetDeliveryCompanyById(id.Id_)))
        );
    }

     public override Task<server.deliverycompany.manager.DeliveryCompanyList> GetDeliveryCompanys(Id id, ServerCallContext context)
    {
        List<server.deliverycompany.DeliveryCompany> deliverycompanys=  _deliverycompanyReader.GetDeliveryCompanys();
        DeliveryCompanyList deliverycompanyList =  new server.deliverycompany.manager.DeliveryCompanyList();

        deliverycompanyList.Deliverycompanys.AddRange(ConvertServerListToGrpc(deliverycompanys));

        return Task.FromResult(
            deliverycompanyList
          
        );
    }

       public override async Task<GeneralResponse> CreateDeliveryCompany(server.deliverycompany.manager.DeliveryCompany deliverycompany, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _deliverycompanyDataWriter.SendAsync(
            new CreateNewDeliveryCompanyCommand(
                ConvertGrpcToServerModel(deliverycompany)
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

       public override async Task<GeneralResponse> UpdateDeliveryCompany(server.deliverycompany.manager.DeliveryCompany deliverycompany, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _deliverycompanyDataWriter.SendAsync(
            new UpdateNewDeliveryCompanyCommand(
                ConvertGrpcToServerModel(deliverycompany)
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

       public override async Task<GeneralResponse> DeleteDeliveryCompany(server.deliverycompany.manager.DeliveryCompany deliverycompany, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _deliverycompanyDataWriter.SendAsync(
            new DeleteNewDeliveryCompanyCommand(
                deliverycompany.Id
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


    public List<server.deliverycompany.manager.DeliveryCompany> ConvertServerListToGrpc(List<server.deliverycompany.DeliveryCompany> deliverycompanys){
        return deliverycompanys.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.deliverycompany.DeliveryCompany ConvertGrpcToServerModel(server.deliverycompany.manager.DeliveryCompany deliverycompany){
        return new server.deliverycompany.DeliveryCompany(deliverycompany.Id, deliverycompany.Address, deliverycompany.Price, deliverycompany.Available,deliverycompany.Name);
    }


    public server.deliverycompany.manager.DeliveryCompany ConvertServerModelToGrpc(server.deliverycompany.DeliveryCompany deliverycompany){
        var deliverycompanyt =  new server.deliverycompany.manager.DeliveryCompany();
        deliverycompanyt.Id = deliverycompany.Id;
        deliverycompanyt.Address = deliverycompany.Address;
        deliverycompanyt.Available = deliverycompany.Available;
        deliverycompanyt.Price = deliverycompany.Price;
        deliverycompanyt.Name = deliverycompany.Name;
        return deliverycompanyt;

    }
}
