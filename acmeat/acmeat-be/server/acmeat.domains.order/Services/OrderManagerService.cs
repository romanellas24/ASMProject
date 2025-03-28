using System.Threading.Tasks;
using acmeat.server.order.manager;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.order.dataproxy;
using acmeat.server.order.datawriter;
using acmeat.server.order;
using System.Collections.Generic;
using Google.Protobuf;
using System.Linq;
using System;

namespace acmeat.domains.order.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcOrderManagerService : server.order.manager.GrpcOrder.GrpcOrderBase
{
    private readonly ILogger<GrpcOrderManagerService> _logger;
    private OrderReader _orderReader;
    private  OrderDataWriter _orderDataWriter;
    public GrpcOrderManagerService(
        ILogger<GrpcOrderManagerService> logger,
        OrderReader orderReader,
        OrderDataWriter orderDataWriter)
    {
        _logger = logger;
        _orderDataWriter = orderDataWriter;
        _orderReader = orderReader;
    }

    public override Task<server.order.manager.HelloReplyClient> SayHello(server.order.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.order.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.order.manager.Order> GetOrderById(Id id, ServerCallContext context)
    {
        return Task.FromResult(
            
           new server.order.manager.Order(ConvertServerModelToGrpc(_orderReader.GetOrderById(id.Id_)))
        );
    }

     public override Task<server.order.manager.OrderList> GetOrders(Id id, ServerCallContext context)
    {
        List<server.order.Order> orders=  _orderReader.GetOrders();
        OrderList orderList =  new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList
          
        );
    }

       public override async Task<GeneralResponse> CreateOrder(server.order.manager.Order order, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _orderDataWriter.SendAsync(
            new CreateNewOrderCommand(
                ConvertGrpcToServerModel(order)
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

       public override async Task<GeneralResponse> UpdateOrder(server.order.manager.Order order, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _orderDataWriter.SendAsync(
            new UpdateNewOrderCommand(
                ConvertGrpcToServerModel(order)
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

       public override async Task<GeneralResponse> DeleteOrder(server.order.manager.Order order, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _orderDataWriter.SendAsync(
            new DeleteNewOrderCommand(
                order.Id
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


    public List<server.order.manager.Order> ConvertServerListToGrpc(List<server.order.Order> orders){
        return orders.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.order.Order ConvertGrpcToServerModel(server.order.manager.Order order){
        return new server.order.Order(
            order.Id,
            order.UserId,
            order.LocalId,
            order.DeliveryCompanyId,
            order.DeliveryTime,
            order.PurchaseTime,
            order.Price,
            order.TransactionId,
            order.MenuId,
            order.Quantity
        );
    }


    public server.order.manager.Order ConvertServerModelToGrpc(server.order.Order order){
        var ordert =  new server.order.manager.Order();
        ordert.Id = order.Id;
        ordert.UserId = order.UserId;
        ordert.LocalId = order.LocalId;
        ordert.DeliveryCompanyId = order.DeliveryCompanyId;
        ordert.DeliveryTime = order.DeliveryTime;
        ordert.PurchaseTime = order.PurchaseTime;
        ordert.Price = order.Price;
        ordert.TransactionId = order.TransactionId;
        ordert.MenuId = order.MenuId;
        ordert.Quantity = order.Quantity;
        return ordert;

    }
}
