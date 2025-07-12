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
using Zeebe.Client;
using NLog.Extensions.Logging;
using System.Text.Json;
using Newtonsoft.Json;
using System.Threading;
using acmeat.server.local.client;
using acmeat.server.deliverycompany.client;
using acmeat.server.user.client;
using acmeat.server.dish.client;
using Microsoft.Extensions.Options;
using acmeat.server.order.client;

namespace acmeat.domains.order.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436




public class GrpcOrderManagerService : server.order.manager.GrpcOrder.GrpcOrderBase
{
    private readonly ILogger<GrpcOrderManagerService> _logger;
    private OrderReader _orderReader;
    private OrderDataWriter _orderDataWriter;
    private readonly LocalClient _localClient;
    private readonly DeliveryCompanyClient _deliveryCompanyClient;
    private readonly UserClient _userClient;
    private readonly DishClient _dishClient;
    private readonly WaitingTimeLocalResponseOptions _waitingTimeLocalResponseOptions;


    private static readonly string ZeebeUrl = "127.0.0.1:26500";
    private static readonly string BPMNDiagramPath = "./Services/Resources/OrderFlowDiagram - Ver1.3b.bpmn";
    private string? BpmnProcessId = "";
    private long? ProcessInstanceKey = 0;
    private static readonly string ProcessInstanceVariables = "{\"a\":\"123\"}";
    private static readonly string JobType = "payment-service";
    private static readonly string WorkerName = Environment.MachineName;
    private static readonly long WorkCount = 100L;

    private ZeebeClient _zeebeClient;
    public GrpcOrderManagerService(
        ILogger<GrpcOrderManagerService> logger,
        OrderReader orderReader,
        OrderDataWriter orderDataWriter,
        LocalClient localClient,
        UserClient userClient,
        DishClient dishClient,
        DeliveryCompanyClient deliveryCompanyClient,
        IOptions<WaitingTimeLocalResponseOptions> waitingTimeOptions
        )
    {
        _logger = logger;
        _orderDataWriter = orderDataWriter;
        _orderReader = orderReader;
        _localClient = localClient;
        _deliveryCompanyClient = deliveryCompanyClient;
        _userClient = userClient;
        _dishClient = dishClient;
        _waitingTimeLocalResponseOptions = waitingTimeOptions.Value;

        _zeebeClient = (ZeebeClient)ZeebeClient.Builder()
.UseLoggerFactory(new NLogLoggerFactory())
.UseGatewayAddress(ZeebeUrl)
.UsePlainText()
.Build();
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
        server.order.manager.OrderList orderList = new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList

        );
    }

    public override Task<server.order.manager.OrderList> GetOrdersByUserId(server.order.manager.Id id, ServerCallContext context)
    {
        List<server.order.Order> orders = _orderReader.GetOrderByUserId(id.Id_);
        server.order.manager.OrderList orderList = new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList

        );
    }

    public override Task<server.order.manager.OrderList> GetOrdersToPay(server.order.manager.Id id, ServerCallContext context)
    {
        List<server.order.Order> orders = _orderReader.GetOrderByUserId(id.Id_).Where(order => order.TransactionId == "").ToList();
        server.order.manager.OrderList orderList = new server.order.manager.OrderList();

        orderList.Orders.AddRange(ConvertServerListToGrpc(orders));

        return Task.FromResult(
            orderList

        );
    }

    public override async Task<server.order.manager.GeneralResponse> CreateOrder(server.order.manager.Order order, ServerCallContext context)
    {
        server.order.manager.GeneralResponse generalResponse = new server.order.manager.GeneralResponse();

        _logger.LogInformation("Started deploying diagram");



        BpmnProcessId = await DeployProcess(BPMNDiagramPath);

        _logger.LogInformation($"BPMN process id: {BpmnProcessId}");


        ProcessInstanceKey = await StartProcessInstance(BpmnProcessId);

        // time to deploy the BPMN diagram
        await Task.Delay(5000);

        //  https://learn.microsoft.com/en-us/dotnet/api/system.threading.tasks.taskcompletionsource-1
        // FOR EVERY DIFFERENT WORKER USE A NEW TASKCOMPLETION
        var tcs = new TaskCompletionSource<server.order.manager.GeneralResponse>();






        var CheckTimeValidityWorker = _zeebeClient
               .NewWorker()
               .JobType("CheckTimeValidity")
               .Handler(async (jobClient, job) =>
               {
                   var localResponse = new server.order.manager.GeneralResponse();

                   try
                   {
                       //    var time = TimeSpan.Parse(order.DeliveryTime);

                       //    if ((time.Hours < 12 || time.Hours > 14) && (time.Hours < 19 || time.Hours > 22))
                       //    {
                       //        localResponse.Message = "Order canceled. You can order only between 12-14 and 19-22";

                       //        await jobClient.NewThrowErrorCommand(job.Key)
                       //            .ErrorCode("OrderNotRespectingTimeRules")
                       //            .ErrorMessage(localResponse.Message)
                       //            .Send();

                       //        tcs.TrySetResult(localResponse);
                       //        return;
                       //    }
                       string jsonStrng = job.Type;

                       await jobClient
                           .NewCompleteJobCommand(job.Key)
                           .Variables(JsonConvert.SerializeObject(order))
                           .Send();

                       localResponse.Message = "OK";
                       tcs.TrySetResult(localResponse);
                   }
                   catch (Exception ex)
                   {
                       localResponse.Message = $"Exception in handler: {ex.Message}";
                       tcs.TrySetException(ex); // opzionale se vuoi catchare nel codice principale
                   }


               })
               .MaxJobsActive(1)
               .Name(Environment.MachineName)
               .PollInterval(TimeSpan.FromSeconds(1))
               .Timeout(TimeSpan.FromSeconds(10))
               .Open();

        // signal.WaitOne(10000);

        generalResponse = await tcs.Task;

        // }
        // ;

        if (generalResponse.Message != "OK")
            return await Task.FromResult(generalResponse);

        DishList dishlist = _dishClient.GetDishsByMenuId(order.MenuId).GetAwaiter().GetResult();
        Local local = _localClient.GetLocalById(order.LocalId).GetAwaiter().GetResult();
        List<DishInfo> dishInfos = dishlist.Dishs.Select(dish => new DishInfo { Id = dish.Id, Quantity = order.Quantity }).ToList();



        var response = await _localClient.CheckOrderAvailabilityServiceWorkerAsync(
    order.DeliveryTime,
   dishInfos,
     local.Url,
    _zeebeClient);


        if (response.Message != "OK")
        {
            generalResponse.Message = response.Message;
            return await Task.FromResult(generalResponse);
        }



        try
        {
            var time = TimeSpan.Parse(order.DeliveryTime);
            var deliveryTime = DateTime.Today.Add(time);
            order.DeliveryTime = deliveryTime.ToString("dd-MM-yyyy HH:mm");

            _orderDataWriter.SendAsync(
             new CreateNewOrderCommand(
                 ConvertGrpcToServerModel(order)
                 )
             ).GetAwaiter().GetResult();
            generalResponse.Message = "OK";



        }
        catch (Exception ex)
        {
            generalResponse.Message = ex.Message;

        }

        return await Task.FromResult(generalResponse);







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

        // TO CHANGE WHEN API IS READY
        // if (order.DeliveryTime != "COMPLETED" || order.DeliveryTime != "TIMEOUT")
        // {
        //     DateTime deliveryTime = DateTime.ParseExact(order.DeliveryTime, "dd-MM-yyyy HH:mm", CultureInfo.InvariantCulture);
        //     _logger.LogInformation($"Current time {now.Hour}:{now.Minute}, delivery time: {order.DeliveryTime}, time between now and delivery time is:{Math.Abs((now - deliveryTime).Hours)} hours");


        //     _logger.LogInformation($"The clause result is: {(now - deliveryTime).Hours}");
        // }



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

    private async Task<string> DeployProcess(String bpmnFile)
    {
        var deployRespone = await _zeebeClient.NewDeployCommand()
            .AddResourceFile(bpmnFile)
            .Send();

        var bpmnProcessId = deployRespone.Processes[0].BpmnProcessId;
        return bpmnProcessId;
    }


    //CAMUNDA UTILITIES

    private async Task<long> StartProcessInstance(string bpmnProcessId)
    {
        var processInstanceResponse = await _zeebeClient
                        .NewCreateProcessInstanceCommand()
                        .BpmnProcessId(bpmnProcessId)
                        .LatestVersion()
                        .Send();

        _logger.LogInformation("Process Instance has been started!");
        var processInstanceKey = processInstanceResponse.ProcessInstanceKey;
        return processInstanceKey;
    }

    public override async Task<server.order.manager.GeneralResponse> HandleLocalAvailabilityResponse(server.order.manager.LocalResponse localResponse, ServerCallContext context)
    {
        _logger.LogInformation($"Handling Local Availabilty response from local with reason: {localResponse}");
        server.order.manager.GeneralResponse resposne = new server.order.manager.GeneralResponse();

        try
        {


            string l = JsonConvert.SerializeObject(localResponse);
            // SIMULATING MESSAGE FROM LOCAL
            await _zeebeClient
.NewPublishMessageCommand()
.MessageName("LocalAvailabitlityResponse")
.CorrelationKey(localResponse.OrderId.ToString()) // deve essere nel context
.Variables(JsonConvert.SerializeObject(localResponse))
.Send();





            var tcs = new TaskCompletionSource<server.order.manager.GeneralResponse>();

            var tcs2 = new TaskCompletionSource<server.order.manager.GeneralResponse>();


            var CancelOrderWorker = _zeebeClient
              .NewWorker()
              .JobType("CancelOrder")
              .Handler(async (jobClient, job) =>
              {
                  var local = new server.order.manager.GeneralResponse();

                  try
                  {
                      _logger.LogInformation("Starting Cancel Order Job");


                      await _orderDataWriter.SendAsync(
                        new DeleteNewOrderCommand(
                        localResponse.OrderId
                        )
                        );

                      await jobClient
                          .NewCompleteJobCommand(job.Key)
                          .Send();

                      switch (localResponse.Reason)
                      {
                          case "TIMEOUT":
                          local.Message = "Order canceled. Local took too much time ";
                              break;

                          case "REJECTED":
                          local.Message = "Order canceled. Local rejected order ";
                              break;

                          default:
                          local.Message = "Order canceled. Uknown reason " + localResponse.Reason;
                              break;
                        }

                      _logger.LogInformation(local.Message);
                      
                      tcs2.TrySetResult(local);
                  }
                  catch (Exception ex)
                  {
                      local.Message = $"Exception in handler: {ex.Message}";
                      tcs2.TrySetException(ex); // opzionale se vuoi catchare nel codice principale
                  }


              })
              .MaxJobsActive(1)
              .Name(Environment.MachineName)
              .PollInterval(TimeSpan.FromSeconds(1))
              .Timeout(TimeSpan.FromSeconds(10))
              .Open();






            var worker = _zeebeClient
                   .NewWorker()
                   .JobType("CheckDeliveryCompanyAvailability")
                   .Handler(async (jobClient, job) =>
                   {
                       _logger.LogInformation("Checking availabilty from delivery company");
                     
                       var localResponse = new server.order.manager.GeneralResponse();
                       // MAKE CALL TO DELIVERY COMPANY

                       localResponse.Message = "OK";
                       await jobClient
                        .NewCompleteJobCommand(job.Key)
                        .Send()
                    ;
                       tcs.TrySetResult(localResponse);

                      

                   })
                   .MaxJobsActive(1)
                   .Name(Environment.MachineName)
                   .PollInterval(TimeSpan.FromSeconds(1))
                   .Timeout(TimeSpan.FromSeconds(10))
                   .Open();


            var taskCompleted = await Task.WhenAny(tcs.Task, tcs2.Task);
            resposne = await taskCompleted;
            return resposne;

        }
        catch (Exception ex)
        {
            resposne.Message = "An error occured: " + ex.Message;
        }

        return resposne;

    }



}

