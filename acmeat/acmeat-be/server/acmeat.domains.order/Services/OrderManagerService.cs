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
// using acmeat.server.local.client;
using System.Globalization;
// using acmeat.server.deliverycompany.client;
// using acmeat.server.user.client;
// using static acmeat.server.deliverycompany.client.DeliveryCompanyClient;
// using acmeat.server.dish.client;
using Telerik.JustMock;
using Microsoft.Extensions.Options;
using System.Threading;

namespace acmeat.domains.order.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436




public class GrpcOrderManagerService : server.order.manager.GrpcOrder.GrpcOrderBase
{
    private readonly ILogger<GrpcOrderManagerService> _logger;
    private OrderReader _orderReader;
    private OrderDataWriter _orderDataWriter;
    // private readonly LocalClient _localClient;
    // private readonly DeliveryCompanyClient _deliveryCompanyClient;
    // private readonly UserClient _userClient;
    // private readonly DishClient _dishClient;
    private readonly WaitingTimeLocalResponseOptions _waitingTimeLocalResponseOptions;
    public GrpcOrderManagerService(
        ILogger<GrpcOrderManagerService> logger,
        OrderReader orderReader,
        // LocalClient localClient,
        // DeliveryCompanyClient deliveryCompanyClient,
        // UserClient userClient,
        // DishClient dishClient,
        OrderDataWriter orderDataWriter,
        IOptions<WaitingTimeLocalResponseOptions>  waitingTimeLocalResponseOptions
        )
    {
        _logger = logger;
        _orderDataWriter = orderDataWriter;
        _orderReader = orderReader;
        // _deliveryCompanyClient = deliveryCompanyClient;
        // _userClient = userClient;
        // _dishClient = dishClient;
        // _localClient = localClient;
        _waitingTimeLocalResponseOptions = waitingTimeLocalResponseOptions.Value;
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

    public override Task<server.order.manager.OrderList> GetOrdersByUserId(server.order.manager.Id id, ServerCallContext context)
    {
        List<server.order.Order> orders = _orderReader.GetOrderByUserId(id.Id_);
        OrderList orderList = new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList

        );
    }

    public override Task<server.order.manager.OrderList> GetOrdersToPay(server.order.manager.Id id, ServerCallContext context)
    {
        List<server.order.Order> orders = _orderReader.GetOrderByUserId(id.Id_).Where(order => order.TransactionId == "").ToList();
        OrderList orderList = new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList

        );
    }

    public override async Task<server.order.manager.GeneralResponse> CreateOrder(server.order.manager.Order order, ServerCallContext context)
    {
        server.order.manager.GeneralResponse generalResponse = new server.order.manager.GeneralResponse();




        var time = TimeSpan.Parse(order.DeliveryTime);
        //CHECK IF THE ORDER IS BETWEEN 12-14 AND 19-22
        if ((time.Hours < 12 || time.Hours > 14) && (time.Hours < 19 || time.Hours > 22))
        {
            generalResponse.Message = "Order must be between 12-14 and 19-22. Cannot proceed with order creation";
            return generalResponse;
        }

        var deliveryTime = DateTime.Today.Add(time);
        order.DeliveryTime = deliveryTime.ToString("dd-MM-yyyy HH:mm");
        // server.order.Order serverOrder = ConvertGrpcToServerModel(order);
        // db.order.Order dbOrder = serverOrder.Convert();
        // Local local = await _localClient.GetLocalById(order.LocalId);
        // User user = await _userClient.GetUserById(order.UserId);
        // CancellationTokenSource s_cts = new CancellationTokenSource();

        //LOCAL AVAILABILITY FLOW
        // DishList dishList = await _dishClient.GetDishsByMenuId(dbOrder.MenuId);
        // List<DishInfo> dishes = dishList.Dishs.AsEnumerable().Select(dish => new DishInfo(dish.Id, order.Quantity)).ToList();

        // server.local.client.GeneralResponse localAvailabilityResponse = new server.local.client.GeneralResponse();
        //OLD VERSION
        // Task checkAvailabilityTask = new Task(async () =>
        // {
        //     localAvailabilityResponse = await _localClient.CheckOrderAvailability(dbOrder, dishes, local.Url);
        // });


        // var hasFinishedSuccesfully = checkAvailabilityTask.Wait(_waitingTimeLocalResponseOptions.Time * 1000);

        // if something has went wrong local-side comunicate to the interface 
        // if (hasFinishedSuccesfully && localAvailabilityResponse.Message != "OK")
        // {

        //     _logger.LogInformation("The local is unavailable the response took too long. Reason" + localAvailabilityResponse.Message);
        //     generalResponse.Message = "The local is unavailable the response took too long. Reason" + localAvailabilityResponse.Message;
        //     return generalResponse;
        // }
        // //if the request took to much time just cancel the flow
        // else if (!hasFinishedSuccesfully)
        // {
        //     _logger.LogInformation("The local is unavailable the response took too long. ");
        //     generalResponse.Message = "The local is unavailable the response took too long.";
        //     return generalResponse;
        // }


        // try
        // {
        //     s_cts.CancelAfter(_waitingTimeLocalResponseOptions.Time * 1000);

        //     localAvailabilityResponse = await _localClient.CheckOrderAvailability(dbOrder, dishes, local.Url, s_cts.Token);

        //     if (localAvailabilityResponse.Message != "OK")
        //     {

        //         _logger.LogInformation("The local is unavailable the response took too long. Reason:" + localAvailabilityResponse.Message);
        //         generalResponse.Message = "The local is unavailable the response took too long. Reason: " + localAvailabilityResponse.Message;
        //         return generalResponse;
        //     }
        // }
        // catch (OperationCanceledException)
        // {
        //     _logger.LogInformation("The local is unavailable the response took too long. ");
        //     generalResponse.Message = "The local is unavailable the response took too long.";
        //     return generalResponse;
        // }
        // finally
        // {
        //     s_cts.Dispose();
        // }



        //DELIVERY COMPANY FLOW

        // DateTime startTime;
        // // DateTime endTime;




        // DeliveryCompanyList deliveryCompanyList = await _deliveryCompanyClient.GetDeliveryCompanyList();
        // DeliveryCompanyList availableCompanies = new DeliveryCompanyList();


        // foreach (DeliveryCompany deliveryCompany in deliveryCompanyList.Deliverycompanys)
        // {
        //     //TO DO:CHECK IF IT'S 10 KM RANGE
        //     if (deliveryCompany.Address != null)
        //     {
        //         _logger.LogInformation($"Checking availability for deliveryCompany with id : {deliveryCompany.Id}");
        //         startTime = DateTime.Now;
        //         // TO DO COUNT UNTIL 15 THEN DELETE THE TASK IF IS NOT RESPONDING
        //         // get veichle information or message justifying the unavailability
        //         var deliveryCompanyResponse = await _deliveryCompanyClient.CheckAvailability(new AvailabilityPayload(local.Address, user.Address, deliveryTime.ToString(), deliveryCompany.Id));



        //         if (deliveryCompanyResponse.Message == "OK")
        //             availableCompanies.Deliverycompanys.Add(deliveryCompany);
        //     }
        // }

        // if (availableCompanies.Deliverycompanys.Count <= 0)
        //     throw new Exception("No delivery company is available");

        // DeliveryCompany bestCompany = availableCompanies.Deliverycompanys.OrderBy(d => d.Price).First();

        // _logger.LogInformation($"Delivery company selected  with id {bestCompany.Id}");

        // await _deliveryCompanyClient.CommunicateOrder(new AvailabilityPayload(local.Address, user.Address, deliveryTime.ToString(), bestCompany.Id));


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
        DateTime deliveryTime = DateTime.ParseExact(order.DeliveryTime, "dd-MM-yyyy HH:mm", CultureInfo.InvariantCulture);
        _logger.LogInformation($"Current time {now.Hour}:{now.Minute}, delivery time: {order.DeliveryTime}, time between now and delivery time is:{Math.Abs((now - deliveryTime).Hours)} hours");

        // if (order.DeliveryTime == "")
        // {
        //     _logger.LogInformation($"Delivery time is not defined cannot delete order with id: {order.Id}");
        //     generalResponse.Message = "Delivery time is not defined cannot delete order with id: " + order.Id + ". Please contact helpdesk";
        //     return generalResponse;

        // }

        // if ((now - deliveryTime).Hours > -1)
        // {
        //     _logger.LogInformation("Cannot delete the order, you can delete it only one hour before the delivery time");
        //     generalResponse.Message = "Cannot delete the order, you can delete it only one hour before the delivery time";
        //     return generalResponse;
        // }

        _logger.LogInformation($"The clause result is: {(now - deliveryTime).Hours}");

        try
        {


            // COMMMUNICATE ORDER CANCELLATION TO LOCAL TO DO
            // Local local = await _localClient.GetLocalById(order.LocalId);
            // var response = await _localClient.CommunicateOrderCancellation(order.Id, local.Url);

            // if (response.Message != "OK")
            // {
            //     _logger.LogInformation("Cannot cancel the order. Reson: " + response.Message);
            //     generalResponse.Message = response.Message;
            // }
            // else
            // {
            //     generalResponse.Message = "OK";
            // }
            await _orderDataWriter.SendAsync(
             new DeleteNewOrderCommand(
                 order.Id
                 )
             );






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
