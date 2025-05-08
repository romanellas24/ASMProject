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
using acmeat.server.local.client;
using System.Globalization;

namespace acmeat.domains.order.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcOrderManagerService : server.order.manager.GrpcOrder.GrpcOrderBase
{
    private readonly ILogger<GrpcOrderManagerService> _logger;
    private OrderReader _orderReader;
    private OrderDataWriter _orderDataWriter;
    private readonly LocalClient _localClient;
    public GrpcOrderManagerService(
        ILogger<GrpcOrderManagerService> logger,
        OrderReader orderReader,
        LocalClient localClient,
        OrderDataWriter orderDataWriter)
    {
        _logger = logger;
        _orderDataWriter = orderDataWriter;
        _orderReader = orderReader;
        _localClient = localClient;
    }

    public override Task<server.order.manager.HelloReplyClient> SayHello(server.order.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.order.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.order.manager.Order> GetOrderById(server.order.manager.Id id, ServerCallContext context)
    {
        return Task.FromResult(

           new server.order.manager.Order(ConvertServerModelToGrpc(_orderReader.GetOrderById(id.Id_)))
        );
    }

    public override Task<server.order.manager.OrderList> GetOrders(server.order.manager.Id id, ServerCallContext context)
    {
        List<server.order.Order> orders = _orderReader.GetOrders();
        OrderList orderList = new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList

        );
    }

    public override async Task<server.order.manager.GeneralResponse> CreateOrder(server.order.manager.Order order, ServerCallContext context)
    {
        server.order.manager.GeneralResponse generalResponse = new server.order.manager.GeneralResponse();
        server.order.Order serverOrder = ConvertGrpcToServerModel(order);
        db.order.Order dbOrder = serverOrder.Convert();
        try
        {
            var response =_localClient.CheckOrderAvailability(dbOrder, dbOrder.LocalId);
            generalResponse.Message = response.Message;
        }
        catch (Exception ex)
        {
            generalResponse.Message = ex.Message;
        }

        if (generalResponse.Message == "OK")
        {
            try
            {
                await _orderDataWriter.SendAsync(
                 new CreateNewOrderCommand(
                     ConvertGrpcToServerModel(order)
                     )
                 );
                generalResponse.Message = "OK";

            }
            catch (Exception ex)
            {
                generalResponse.Message = ex.Message;
            }
            return await Task.FromResult(
                generalResponse
            );

        }
        else
        {
            return generalResponse;
        }



    }

    public override async Task<server.order.manager.GeneralResponse> UpdateOrder(server.order.manager.Order order, ServerCallContext context)
    {
        server.order.manager.GeneralResponse generalResponse = new server.order.manager.GeneralResponse();
        try
        {
            await _orderDataWriter.SendAsync(
             new UpdateNewOrderCommand(
                 ConvertGrpcToServerModel(order)
                 )
             );
            generalResponse.Message = "OK";

        }
        catch (Exception ex)
        {
            generalResponse.Message = ex.Message;
        }
        return await Task.FromResult(
            generalResponse
        );

    }

    public override async Task<server.order.manager.GeneralResponse> DeleteOrder(server.order.manager.Order order, ServerCallContext context)
    {
        server.order.manager.GeneralResponse generalResponse = new server.order.manager.GeneralResponse();
        DateTime now = DateTime.Now;
        DateTime deliveryTime = DateTime.ParseExact(order.DeliveryTime,"HH:mm",CultureInfo.InvariantCulture);
        _logger.LogInformation($"Current time {now.Hour}:{now.Minute}, delivery time: {order.DeliveryTime}, time between now and delivery time is:{Math.Abs((now - deliveryTime).Hours)} hours");

        if(Math.Abs((now - deliveryTime).Hours) > 1){
            _logger.LogInformation("Cannot delete the order, you can delete it only one hour before the delivery time");
            generalResponse.Message  = "Cannot delete the order, you can delete it only one hour before the delivery time";
            return generalResponse;
        }

        try
        {
            await _orderDataWriter.SendAsync(
             new DeleteNewOrderCommand(
                 order.Id
                 )
             );
            generalResponse.Message = "OK";

        }
        catch (Exception ex)
        {
            generalResponse.Message = ex.Message;
        }
        return await Task.FromResult(
            generalResponse
        );

    }


    public List<server.order.manager.Order> ConvertServerListToGrpc(List<server.order.Order> orders)
    {
        return orders.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.order.Order ConvertGrpcToServerModel(server.order.manager.Order order)
    {
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


    public server.order.manager.Order ConvertServerModelToGrpc(server.order.Order order)
    {
        var ordert = new server.order.manager.Order();
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
