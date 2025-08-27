using System.Threading.Tasks;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.order.dataproxy;
using acmeat.server.order.datawriter;
using System.Collections.Generic;
using System.Linq;
using System;
using System.Globalization;
using Zeebe.Client;
using NLog.Extensions.Logging;
using Newtonsoft.Json;
using acmeat.server.local.client;
using acmeat.server.deliverycompany.client;
using acmeat.server.user.client;
using acmeat.server.dish.client;
using Microsoft.Extensions.Options;
using GeneralResponse = acmeat.server.order.manager.GeneralResponse;
using static acmeat.server.deliverycompany.client.DeliveryCompanyClient;
using Zeebe.Client.Api.Worker;
using Newtonsoft.Json.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using acmeat.db.order;

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
    private readonly ZeebeClientOptions _zeebeClientOptions;
    // "ConnectionString":"127.0.0.1:26500"


    private static readonly string BPMNDiagramPath = "Services/Resources/OrderFlowDiagram - Ver1.3b.bpmn";
    private string? BpmnProcessId = "";
    private long? ProcessInstanceKey = 0;

    private ZeebeClient _zeebeClient;
    public GrpcOrderManagerService(
        ILogger<GrpcOrderManagerService> logger,
        OrderReader orderReader,
        OrderDataWriter orderDataWriter,
        LocalClient localClient,
        UserClient userClient,
        DishClient dishClient,
        DeliveryCompanyClient deliveryCompanyClient,
        IOptions<WaitingTimeLocalResponseOptions> waitingTimeOptions,
        IOptions<ZeebeClientOptions> zeebeClientOptions
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
        _zeebeClientOptions = zeebeClientOptions.Value;

        _zeebeClient = (ZeebeClient)ZeebeClient.Builder()
.UseLoggerFactory(new NLogLoggerFactory())
.UseGatewayAddress(_zeebeClientOptions.ConnectionString)
.UsePlainText()
.Build();


        ProcessOrderWorker();
        CheckLocalAvailabiltyWorker();
        CheckDeliveryCompanyAvailabilityWorker();
        CommunicateOrderToDeliveryCompany();
        CommunicateOrderCancellationLocalWorker();
        CommunicateOrderCancellationDeliveryCompany();
        SendRefund();
        CancelOrderWorker();

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
        DateTime now = DateTime.Now;
        List<server.order.Order> orders = _orderReader.GetOrderByUserId(id.Id_)
        .Where(order => order.TransactionId == "")
        .Where(order => order.DeliveryTime == "" || DateTime.Parse(order.DeliveryTime) > now) 
        .ToList();
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
        // time to deploy the BPMN diagram
        // await Task.Delay(5000);

        _logger.LogInformation($"BPMN process id: {BpmnProcessId}");


        ProcessInstanceKey = await StartProcessInstance(BpmnProcessId, order);


        generalResponse.Message = "OK";



        return generalResponse;







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

        //warn CAMUNDA that we have received a cancellation order
        await _zeebeClient
        .NewPublishMessageCommand()
        .MessageName("OrderCancellationMessage")
        .CorrelationKey(order.Id.ToString())
        .Send();

        TaskCompletionSource<GeneralResponse> tcs = new TaskCompletionSource<GeneralResponse>();
        CheckOrderCancellationRulesWorker(order, tcs);

        var completed = await tcs.Task;

        //IF EVERYTHING IS OK WE DELETE THE ORDER
        if (completed.Message == "OK")
        {
            try
            {

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
        }
        generalResponse.Message = completed.Message;

        return await Task.FromResult(
            generalResponse
        );

    }


     public override async Task<server.order.manager.GeneralResponse> DeleteOrderForced(server.order.manager.Order order, ServerCallContext context)
    {


        server.order.manager.GeneralResponse generalResponse = new server.order.manager.GeneralResponse();
        DateTime now = DateTime.Now;


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
    public IJobWorker? CheckOrderCancellationRulesWorker(server.order.manager.Order order, TaskCompletionSource<GeneralResponse> tcs)
    {
        return _zeebeClient
               .NewWorker()
               .JobType("CheckOrderCancellationRules")
               .Handler(async (jobClient, job) =>
               {
                   var localResponse = new server.order.manager.GeneralResponse();



                   try
                   {
                       DateTime deliveryTime = DateTime.Parse(order.DeliveryTime);
                       DateTime now = DateTime.Now;



                       if ((now - deliveryTime).Hours > -1)
                       {
                           localResponse.Message = "Cannot delete the order, you can delete it only one hour before the delivery time";
                           _logger.LogInformation($"{localResponse.Message}");

                           await jobClient
                           .NewCompleteJobCommand(job.Key)
                           .Variables(JsonConvert.SerializeObject(new { IsRespectingRules = false }))
                           .Send();
                       }
                       else
                       {
                           _logger.LogInformation($"Order Cancellation is respecting rules. Proceed cancelling order {order.Id}");
                           await jobClient
                             .NewCompleteJobCommand(job.Key)
                             .Variables(JsonConvert.SerializeObject(new { IsRespectingRules = true }))
                             .Send();
                           localResponse.Message = "OK";

                       }



                       tcs.TrySetResult(localResponse);
                   }
                   catch (Exception ex)
                   {
                       localResponse.Message = $"Exception in handler: {ex.Message}";
                       tcs.TrySetException(ex);
                   }


               })
               .MaxJobsActive(1)
               .Name(Environment.MachineName)
               .PollInterval(TimeSpan.FromSeconds(1))
               .Timeout(TimeSpan.FromSeconds(30))
               .Open();
    }




    public override async Task<server.order.manager.GeneralResponse> VerifyPayment(server.order.manager.Payment payment, ServerCallContext context)
    {
        GeneralResponse response = new GeneralResponse();

        try
        {
            // SIMULATING MESSAGE FROM LOCAL
            await _zeebeClient
.NewPublishMessageCommand()
.MessageName("PaymentInfo")
.CorrelationKey(payment.OrderId.ToString()) // deve essere nel context
.Variables(JsonConvert.SerializeObject(new
{
    TransactionId = payment.TransactionId,
    OrderId = payment.OrderId
}
))
.Send();
            TaskCompletionSource<GeneralResponse> tcs = new TaskCompletionSource<GeneralResponse>();

            VerifyPaymentWorker(payment, tcs);

            response = await tcs.Task;


        }
        catch (Exception ex)
        {
            response.Message = "An error occured: " + ex.Message;
        }

        return response;
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
        _logger.LogInformation($"Process deployed with id {bpmnProcessId}");
        return bpmnProcessId;
    }


    //CAMUNDA UTILITIES

    private async Task<long> StartProcessInstance(string bpmnProcessId, server.order.manager.Order order)
    {
        order.DeliveryTime = NormalizeTime(order.DeliveryTime);
        var processInstanceResponse = await _zeebeClient
                        .NewCreateProcessInstanceCommand()
                        .BpmnProcessId(bpmnProcessId)
                        .LatestVersion()
                        .Variables(JsonConvert.SerializeObject(order))
                        .Send();


        var processInstanceKey = processInstanceResponse.ProcessInstanceKey;
        _logger.LogInformation($"Process Instance has been started with id {processInstanceKey}!");
        return processInstanceKey;
    }

    private void ProcessOrderWorker()
    {
        _zeebeClient
             .NewWorker()
             .JobType("ProcessOrder")
             .Handler(async (jobClient, job) =>
             {
                 var localResponse = new server.order.manager.GeneralResponse();

                 try
                 {
                     var variablesJson = job.Variables;
                     var orderFromJob = JsonConvert.DeserializeObject<server.order.manager.Order>(variablesJson);

                     _logger.LogInformation($"Processing order with ID: {orderFromJob.Id}");
                     DateTime time = DateTime.Parse(orderFromJob.DeliveryTime);

                     bool isLunchTime = time.Hour >= 12 && time.Hour <= 14;
                     bool isDinnerTime = time.Hour >= 19 && time.Hour <= 21;

                     if (!isLunchTime && !isDinnerTime)
                     {
                         await _zeebeClient
                         .NewThrowErrorCommand(job.Key)
                         .ErrorCode("500")
                         .ErrorMessage("You can place orders between 12-14 and 19-22")
                         .Send();
                         localResponse.Message = "You can place orders between 12-14 and 19-22";
                         _logger.LogInformation(localResponse.Message);
                     }
                     else
                     {
                         //TO AVOID PROBLEMS WITH DATE

                         orderFromJob.DeliveryTime = NormalizeTime(orderFromJob.DeliveryTime);
                         _logger.LogInformation($"Starting creating Order... with Id {orderFromJob.Id}");
                         await _orderDataWriter.SendAsync(
                           new CreateNewOrderCommand(
                               ConvertGrpcToServerModel(orderFromJob)
                               )
                           );
                         _logger.LogInformation($"Order created with Id {orderFromJob.Id}");


                         localResponse.Message = "OK";
                         await jobClient
   .NewCompleteJobCommand(job.Key)
   .Send();
                         _logger.LogInformation("Published order");
                         //   tcs.TrySetResult(localResponse);

                     }



                 }
                 catch (Exception ex)
                 {
                     localResponse.Message = $"Exception in handler: {ex.Message}";
                     //    await _zeebeClient
                     //         .NewThrowErrorCommand(job.Key)
                     //         .ErrorCode("500")
                     //         .ErrorMessage(localResponse.Message)
                     //         .Send();
                     _logger.LogInformation(localResponse.Message);

                     //   tcs.TrySetException(ex);
                 }


             })
             .MaxJobsActive(1)
             .Name(Environment.MachineName)
             .PollInterval(TimeSpan.FromSeconds(1))
             .Timeout(TimeSpan.FromSeconds(10))
             .Open();

    }


    public void CheckLocalAvailabiltyWorker()
    {



        _zeebeClient
              .NewWorker()
              .JobType("CheckLocalAvailabilty")
              .Handler(async (jobClient, job) =>
              {
                  _logger.LogInformation($"Starting CheckAvailability for locals");
                  var variables = job.Variables;
                  JObject root = JObject.Parse(variables);
                  var orderJson = root["Order"].ToString();
                  server.order.manager.Order? order = JsonConvert.DeserializeObject<server.order.manager.Order>(orderJson);

                  if (order != null)
                  {
                      _logger.LogInformation($"Getting dishes from menu {order.MenuId}");

                      DishList dishlist = await _dishClient.GetDishsByMenuId(order.MenuId);

                      _logger.LogInformation($"Getting local with Id {order.LocalId}");

                      Local local = await _localClient.GetLocalById(order.LocalId);
                      List<DishInfo> dishInfos = dishlist.Dishs.Select(dish => new DishInfo { Id = dish.Id, Quantity = order.Quantity }).ToList();






                      order.DeliveryTime = NormalizeTime(order.DeliveryTime);

                      _logger.LogInformation($"Contacting {local.Url} for check its availability");
                      var response = await _localClient.CheckOrderAvailability(order.Id, order.DeliveryTime, dishInfos, local.Url);

                      //TO DECOMMENT
                      if (response.Message == "OK")
                      {

                          _logger.LogInformation($"Local {local.Url} is available");
                          await jobClient
                               .NewCompleteJobCommand(job.Key)
                               .Send()
                           ;
                          _logger.LogInformation($"Check Local Availability Job is completed");


                      }
                      else
                      {
                          await jobClient.NewThrowErrorCommand(job.Key)
                  .ErrorCode("500")
                  .ErrorMessage($"{response.Message}")
                  .Send();
                          _logger.LogInformation($"Something went wrong checking awailability: {response.Message}");
                      }

                  }
                  else
                  {
                      _logger.LogInformation($"Failed to retrieve order. Is null");
                      await jobClient.NewThrowErrorCommand(job.Key)
                .ErrorCode("500")
                .ErrorMessage($"order is null")
                .Send();


                  }


              })
              .MaxJobsActive(1)
              .Name(Environment.MachineName)
              .PollInterval(TimeSpan.FromSeconds(1))
              .Timeout(TimeSpan.FromSeconds(10))
              .Open();



    }

    private void SendRefund()
    {
        GeneralResponse generalResponse = new GeneralResponse();
        HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("https://joliebank.romanellas.cloud/"),
        };
        _zeebeClient
            .NewWorker()
            .JobType("SendRefund")
            .Handler(async (jobClient, job) =>
            {

                try
                {
                    _logger.LogInformation("Starting SendRefund Job");
                    var variables = job.Variables;
                    // Parsare l'oggetto Order
                    JObject root = JObject.Parse(variables);
                    var paymentJson = root["PaymentInformation"].ToString();
                    server.order.manager.Payment? payment = JsonConvert.DeserializeObject<server.order.manager.Payment>(paymentJson);


                    if (payment != null)
                    {

                        _logger.LogInformation($"Payment with transactionId {payment.TransactionId} starting refound");
                        HttpResponseMessage? bankTransctiondeletion = await sharedClient.DeleteAsync(sharedClient.BaseAddress + "payments/" + payment.TransactionId);
                        if (bankTransctiondeletion != null && bankTransctiondeletion.StatusCode == System.Net.HttpStatusCode.OK)
                        {

                            _logger.LogInformation($"refound started");


                        }
                        else
                        {

                            _logger.LogInformation($"an error occured during transaction deletion code:{bankTransctiondeletion?.StatusCode} content: {bankTransctiondeletion?.Content}");
                        }



                        await jobClient
                           .NewCompleteJobCommand(job.Key)
                           .Send();
                        _logger.LogInformation("Send Refund job completed");

                        //SEND REFOUND RESPONSE
                        await _zeebeClient
                        .NewPublishMessageCommand()
                        .MessageName("RefundResponse")
                        .CorrelationKey(payment.OrderId.ToString())
                        .Variables(

                             JsonConvert.SerializeObject(
                                 new
                                 {
                                     RefoundResponse = bankTransctiondeletion?.Content
                                 }
                             )
                           )
                        .Send();
                        _logger.LogInformation("Publishing message ");
                    }
                    else
                    {
                        _logger.LogInformation("Payment information is null!");
                        throw new Exception("Payment information is null");
                    }

                }
                catch (Exception ex)
                {

                    _logger.LogInformation($"Exception in handler: {ex.Message}");
                    await _zeebeClient
                    .NewThrowErrorCommand(job.Key)
                    .ErrorCode("500")
                    .ErrorMessage(ex.Message)
                    .Send();
                }


            })
            .MaxJobsActive(1)
            .Name(Environment.MachineName)
            .PollInterval(TimeSpan.FromSeconds(1))
            .Timeout(TimeSpan.FromSeconds(10))
            .Open();



    }


    private void CommunicateOrderCancellationLocalWorker()
    {
        _zeebeClient
             .NewWorker()
             .JobType("CommunicateOrderCancellationLocal")
             .Handler(async (jobClient, job) =>
             {

                 try
                 {
                     _logger.LogInformation("Starting CommunicateOrderCancellationLocal Job");
                     var variables = job.Variables;
                     // Parsare l'oggetto Order
                     JObject root = JObject.Parse(variables);
                     var orderJson = root["Order"].ToString();
                     server.order.manager.Order? order = JsonConvert.DeserializeObject<server.order.manager.Order>(orderJson);
                     Local local1 = await _localClient.GetLocalById(order.LocalId);


                     if (order != null)
                     {
                         _logger.LogInformation($"Communicating cancellation to {local1.Url} order with id {order.Id} ");

                         var response = await _localClient.CommunicateOrderCancellation(order.Id, local1.Url);

                         _logger.LogInformation($"Local response {response.Message}");

                         await jobClient
                             .NewCompleteJobCommand(job.Key)
                             .Send();
                         _logger.LogInformation($"CommunicateOrderCancellationLocal Finished succesfully");


                         await _zeebeClient
                         .NewPublishMessageCommand()
                         .MessageName("LocalOrderCancellationResponse")
                         .CorrelationKey(order.Id.ToString())
                         .Variables(

                              JsonConvert.SerializeObject(
                                  new
                                  {
                                      LocalResponse = response.Message
                                  }
                              )
                            )
                         .Send();
                         _logger.LogInformation($"Publishing the message of LocalOrderCancellationResponse");
                     }
                     else
                     {
                         _logger.LogInformation("Order is null");
                         throw new Exception("Cannot deserialize order is null");
                     }

                 }
                 catch (Exception ex)
                 {
                     _logger.LogInformation($"{ex.Message}");
                     await _zeebeClient
                     .NewThrowErrorCommand(job.Key)
                     .ErrorCode("500")
                     .ErrorMessage(ex.Message)
                     .Send();
                 }


             })
             .MaxJobsActive(1)
             .Name(Environment.MachineName)
             .PollInterval(TimeSpan.FromSeconds(1))
             .Timeout(TimeSpan.FromSeconds(10))
             .Open();
    }

    public void CommunicateOrderCancellationDeliveryCompany()
    {
        _zeebeClient
             .NewWorker()
             .JobType("CommunicateOrderCancellationDeliveryCompany")
             .Handler(async (jobClient, job) =>
             {


                 try
                 {
                     _logger.LogInformation("Starting CommunicateOrderCancellationLocal Job");
                     var variables = job.Variables;
                     // Parsare l'oggetto Order
                     JObject root = JObject.Parse(variables);
                     var DeliveryCompanyAvailabilityResponse = root["DeliveryCompanyAvailabilityResponse"].ToString();

                     DeliveryCompanyPayload? payload = JsonConvert.DeserializeObject<DeliveryCompanyPayload>(DeliveryCompanyAvailabilityResponse);

                     if (payload != null)
                     {

                         _logger.LogInformation($"Payload {payload} retrieved. Sending order cancellation to delivery company {payload.DeliveryCompanyUrl}.");
                         var response = await _deliveryCompanyClient.SendOrderCancellationToDeliveryCompany(payload.OrderId, payload.DeliveryCompanyUrl);
                         _logger.LogInformation($"Delivery company resonse: {response.Message}");

                         await jobClient
                             .NewCompleteJobCommand(job.Key)
                             .Send();


                         _logger.LogInformation("Delivery Company order cancellation completed");

                         //   tcs.TrySetResult(local);
                         await _zeebeClient
                       .NewPublishMessageCommand()
                       .MessageName("DeliveryCompanyOrderCancellationResponse")
                       .CorrelationKey(payload.OrderId.ToString())
                       .Variables(

                            JsonConvert.SerializeObject(
                                new
                                {
                                    DeliveryComapnyResponse = response.Message
                                }
                            )
                          )
                       .Send();

                         _logger.LogInformation($"Published message for DeliveryComapny order cancellation");
                     }
                     else
                     {
                         throw new Exception("Cannot deserialize order is null");
                     }

                 }
                 catch (Exception ex)
                 {

                     _logger.LogInformation($"{ex.Message}");
                     await _zeebeClient
                     .NewThrowErrorCommand(job.Key)
                     .ErrorCode("500")
                     .ErrorMessage(ex.Message)
                     .Send();
                     //  await _zeebeClient.NewCancelInstanceCommand((long)ProcessInstanceKey).Send();
                     //   tcs.TrySetException(ex); // opzionale se vuoi catchare nel codice principale
                 }


             })
             .MaxJobsActive(1)
             .Name(Environment.MachineName)
             .PollInterval(TimeSpan.FromSeconds(1))
             .Timeout(TimeSpan.FromSeconds(10))
             .Open();
    }


    private void CancelOrderWorker()
    {
        _zeebeClient
             .NewWorker()
             .JobType("CancelOrder")
             .Handler(async (jobClient, job) =>
             {
                 var local = new server.order.manager.GeneralResponse();

                 try
                 {
                     _logger.LogInformation("Starting Cancel Order Job");
                     var variables = job.Variables;
                     // Parsare l'oggetto Order
                     JObject root = JObject.Parse(variables);
                     var orderJson = root["Order"].ToString();
                     server.order.manager.Order? order = JsonConvert.DeserializeObject<server.order.manager.Order>(orderJson);

                     if (order != null)
                     {



                         _logger.LogInformation($"Retrieved order to cancel with id: {order.Id}");
                         await _orderDataWriter.SendAsync(
                         new DeleteNewOrderCommand(
                             order.Id
                         )
                         );

                         _logger.LogInformation($"Cancelling order with id: {order.Id} has been sucesfully");

                         await jobClient
                             .NewCompleteJobCommand(job.Key)
                             .Send();


                         _logger.LogInformation($" Order cancellatio has finished succesfully!");

                         //   tcs.TrySetResult(local);
                     }
                     else
                     {
                         throw new Exception("Cannot deserialize order is null");
                     }

                 }
                 catch (Exception ex)
                 {
                     local.Message = $"Exception in handler: {ex.Message}";
                     _logger.LogInformation($"{local.Message}");
                     //  await _zeebeClient.NewCancelInstanceCommand((long)ProcessInstanceKey).Send();
                     //   tcs.TrySetException(ex); // opzionale se vuoi catchare nel codice principale
                 }


             })
             .MaxJobsActive(1)
             .Name(Environment.MachineName)
             .PollInterval(TimeSpan.FromSeconds(1))
             .Timeout(TimeSpan.FromSeconds(10))
             .Open();

    }

    //REMOVE ONLY IF CHECK DELIVERY COMPANY AVAILABILITY IS OK
    private void CommunicateOrderToDeliveryCompany()
    {
        _zeebeClient
              .NewWorker()
              .JobType("AllocateVehicle")
              .Handler(async (jobClient, job) =>
              {


                  try
                  {
                      _logger.LogInformation("Starting Communicating Order to DeliveryCompany Job");

                      var variables = job.Variables;
                      // Parsare l'oggetto Order
                      JObject root = JObject.Parse(variables);
                      var DeliveryCompanyAvailabilityResponse = root["DeliveryCompanyAvailabilityResponse"].ToString();

                      DeliveryCompanyPayload? payload = JsonConvert.DeserializeObject<DeliveryCompanyPayload>(DeliveryCompanyAvailabilityResponse);

                      _logger.LogInformation($"Comunicating to delivery Company {payload.DeliveryCompanyUrl}");
                      var response = await _deliveryCompanyClient.CommunicateOrderToDeliveryCompany(
                        payload.OrderId,
                        new DeliveryCompanyAvailabilityResponse2v
                        {
                            DeliveryCompanyUrl = payload.DeliveryCompanyUrl,
                            distance = payload.Distance,
                            isVehicleAvailable = payload.IsVehicleAvailable,
                            price = payload.Price,
                            time = payload.TimeMinutes,
                            vehicleId = payload.Vehicle


                        },
                            new AvailabilityPayload
                            (
                                payload.LocalAddress,
                                payload.UserAddress,
                                payload.DeliveryTime,
                                payload.DeliveryCompanyUrl
                            )

                            );

                      _logger.LogInformation($"Delivery company response {response.Message}");
                      if (response.Message == "OK")
                      {
                          await jobClient
                            .NewCompleteJobCommand(job.Key)
                            //   .Variables("{OK}")
                            .Send();

                          _logger.LogInformation($"Comunicate Order To Delivery Company has finished succesfully!");

                          await _zeebeClient
                            .NewPublishMessageCommand()
                            .MessageName("AllocateVehicleResponse")
                            .CorrelationKey(payload.OrderId.ToString())
                            .Variables(JsonConvert.SerializeObject(new
                            {
                                Response = response.Message
                            }))
                            .Send();



                      }
                      else
                      {
                          await jobClient.NewThrowErrorCommand(job.Key)
                        .ErrorCode("500")
                        .ErrorMessage(response.Message)
                        .Send();
                          _logger.LogInformation($"Something went wrong with the Communicating Order to delivery Company. Reason: {response.Message}");
                      }

                      _logger.LogInformation(response.Message);

                  }
                  catch (Exception ex)
                  {
                      _logger.LogInformation($"Exception during COmmunicate Order to Delivery Company: {ex.Message}");
                  }


              })
              .MaxJobsActive(1)
              .Name(Environment.MachineName)
              .PollInterval(TimeSpan.FromSeconds(1))
              .Timeout(TimeSpan.FromSeconds(10))
              .Open();


    }


    public void CheckDeliveryCompanyAvailabilityWorker()
    {
        _ = _zeebeClient
                  .NewWorker()
                  .JobType("CheckDeliveryCompanyAvailability")
                  .Handler(async (jobClient, job) =>
                  {
                      _logger.LogInformation("Checking availabilty from delivery company");

                      try
                      {

                          var variables = job.Variables;
                          // Parsare l'oggetto Order
                          JObject root = JObject.Parse(variables);
                          var orderJson = root["Order"].ToString();
                          server.order.manager.Order? order = JsonConvert.DeserializeObject<server.order.manager.Order>(orderJson);

                          if (order != null)
                          {

                              _logger.LogInformation($"Retrieved order with Id: {order?.Id}");
                              //FETCH INFOS RELEVANT  TO THE DELIVERY COMPANY AND USER
                              _logger.LogInformation($"Getting local with Id :{order?.LocalId}");
                              Local localInfo = await _localClient.GetLocalById(order.LocalId);

                              _logger.LogInformation($"Getting user with Id: {order.UserId}");
                              User user = await _userClient.GetUserById(order.UserId);

                              _logger.LogInformation($"Getting delivery companies");
                              List<DeliveryCompany> deliveryCompanies = (await _deliveryCompanyClient.GetDeliveryCompanyList()).Deliverycompanys.ToList();
                              List<string> deliveryCompanyUrls = deliveryCompanies.Select(deliveryCompany => deliveryCompany.Name).ToList();


                              AvailabilityPayload availabiltyPayload;
                              List<Task<DeliveryCompanyAvailabilityResponse2v>> deliveryCompanysResponses = new List<Task<DeliveryCompanyAvailabilityResponse2v>>();
                              List<DeliveryCompanyAvailabilityResponse2v> deliveryCompanyAvailabilityResponses = new List<DeliveryCompanyAvailabilityResponse2v>();


                              // PREPAIRING CHECK AVAILABILITY CALLS
                              _logger.LogInformation("Prepairing to call CheckAvailability for Delivery Company");
                              foreach (string deliveryCompanyUrl in deliveryCompanyUrls)
                              {

                                  availabiltyPayload = new AvailabilityPayload
                                  (
                                      localInfo.Address,
                                      user.Address,
                                      NormalizeTime(order.DeliveryTime),
                                      deliveryCompanyUrl

                                  );

                                  deliveryCompanysResponses.Add(_deliveryCompanyClient.CheckAvailabilityWorker(availabiltyPayload));



                              }

                              // HERE WAIT THE RESPONSES FOR 15 SECONDS
                              var allTasks = deliveryCompanysResponses;
                              var timeoutTask = Task.Delay(15000);
                              // to decomment
                              // Wait for every task or exit prematurely due to time out 
                              _logger.LogInformation("Start checking availability...");
                              foreach (var task in allTasks)
                              {
                                  var finished = await Task.WhenAny(task, timeoutTask);


                                  try
                                  {
                                      // Add result if is successfully
                                      if (finished != timeoutTask)
                                      {
                                          var result = await task;
                                          _logger.LogInformation($"Check availability for {result.DeliveryCompanyUrl} is :{result.isVehicleAvailable}");
                                          deliveryCompanyAvailabilityResponses.Add(result);
                                      }
                                  }
                                  catch (Exception ex)
                                  {
                                      _logger.LogError(ex.Message);
                                      //    throw ex; 
                                  }



                              }



                              deliveryCompanyAvailabilityResponses = deliveryCompanyAvailabilityResponses.Where(response => response.distance < 10).ToList();

                              //IF NO DELIVERY COMPANY IS AVAILABLE WE PUBLISH THE MESSAGE
                              if (deliveryCompanyAvailabilityResponses.Count <= 0)
                              {
                                  _logger.LogInformation("No delivery company available near the user");
                                  await _zeebeClient
                              .NewThrowErrorCommand(job.Key)
                              .ErrorCode("500")
                              .ErrorMessage("No delivery company available near the user")
                              .Send();

                                  //OTHERWISE WE SELECT THE CHEAPEST ONE AND WE PUBLISH IT
                              }
                              else
                              {
                                  _logger.LogInformation($"Selecting the delivery company....");
                                  DeliveryCompanyAvailabilityResponse2v? del = deliveryCompanyAvailabilityResponses.Find(
                                     deliveryC =>
                                      deliveryC.price == deliveryCompanyAvailabilityResponses.Min(
                                         del => del.price
                                         ));
                                  var Payload = new
                                  {
                                      LocalAddress = localInfo.Address,
                                      UserAddress = user.Address,
                                      DeliveryTime = NormalizeTime(order.DeliveryTime),
                                      DeliveryCompanyUrl = del?.DeliveryCompanyUrl,
                                      OrderId = order.Id,
                                      Vehicle = del?.vehicleId,
                                      TimeMinutes = del?.time,
                                      IsVehicleAvailable = del?.isVehicleAvailable,
                                      Price = del?.price,
                                      Distance = del?.distance

                                  };
                                  // CHECK DELIVERY COMPANY AVAILABILTY TASK OK 
                                  await jobClient
                                   .NewCompleteJobCommand(job.Key)
                                   .Send();


                                  _logger.LogInformation($"Check availabiltiy is OK. Selected {del.DeliveryCompanyUrl} with price minimum: {del.price}");
                                  await _zeebeClient
                                 .NewPublishMessageCommand()
                                 .MessageName("DeliveryCompanyAvailabilityResponse")
                                 .CorrelationKey(order.Id.ToString())
                                 .Variables(JsonConvert.SerializeObject(Payload))
                                 .Send();

                                  if (del != null)
                                  {
                                      DeliveryCompany? deliveryCompany = deliveryCompanies
                                  .Find(deliveryCompany =>
                                   deliveryCompany.Name
                                   .Equals(del.DeliveryCompanyUrl));

                                      if (deliveryCompany != null)
                                      {
                                          order.DeliveryCompanyId = deliveryCompany.Id;
                                          _logger.LogInformation($"Updating Order with id {order.Id}. Adding deliveryCompany id {deliveryCompany.Id}");
                                          await _orderDataWriter.SendAsync(
                                             new UpdateNewOrderCommand(
                                                ConvertGrpcToServerModel(order)
                                            )
                                        );

                                          _logger.LogInformation("Update to order finished");

                                      }

                                  }


                                  _logger.LogInformation($"using the following vehicle: {Payload.Vehicle}");

                              }

                          }
                          else
                          {
                              await _zeebeClient
                          .NewThrowErrorCommand(job.Key)
                          .ErrorCode("500")
                          .ErrorMessage("Error is Null!")
                          .Send();
                          }

                      }
                      catch (Exception ex)
                      {


                          _logger.LogInformation($"An Exception Occurred: {ex.Message}");
                      }

                  })
                  .MaxJobsActive(1)
                  .Name(Environment.MachineName)
                  .PollInterval(TimeSpan.FromSeconds(1))
                  .Timeout(TimeSpan.FromSeconds(30))
                  .Open();


        ;
    }




    public override async Task<server.order.manager.GeneralResponse> HandleLocalAvailabilityResponse(server.order.manager.LocalResponse localResponse, ServerCallContext context)
    {
        _logger.LogInformation($"Handling Local Availabilty response from local with reason: {localResponse}");
        server.order.manager.GeneralResponse resposne = new server.order.manager.GeneralResponse();

        try
        {
            // SIMULATING MESSAGE FROM LOCAL

            _logger.LogInformation($"Order id :{localResponse.OrderId} Reason:{localResponse.Reason}");
            await _zeebeClient
.NewPublishMessageCommand()
.MessageName("LocalAvailabilityResponse")
.CorrelationKey(localResponse.OrderId.ToString()) // deve essere nel context
.Variables(JsonConvert.SerializeObject(new
{
    OrderId = localResponse.OrderId,
    Reason = localResponse.Reason
}
))
.Send();

            resposne.Message = "OK";


        }
        catch (Exception ex)
        {
            resposne.Message = "An error occured: " + ex.Message;
            _logger.LogError(ex.Message);
        }

        return resposne;

    }

    private IJobWorker? VerifyPaymentWorker(server.order.manager.Payment payment, TaskCompletionSource<server.order.manager.GeneralResponse> tcs)
    {
        return _zeebeClient
              .NewWorker()
              .JobType("CheckPaymentInfo")
              .Handler(async (jobClient, job) =>
              {
                  var localResponse = new server.order.manager.GeneralResponse();

                  try
                  {
                      _logger.LogInformation("Starting verify Payment job...");

                      HttpClient sharedClient = new()
                      {
                          BaseAddress = new Uri("https://joliebank.romanellas.cloud/"),
                      };
                      _logger.LogInformation($"Contacing Bank at {sharedClient.BaseAddress + "payments/" + payment.TransactionId}");

                      BankPaymentVerification? bankPaymentVerification = await sharedClient.GetFromJsonAsync<BankPaymentVerification>(sharedClient.BaseAddress + "payments/" + payment.TransactionId);
                      if (bankPaymentVerification != null && bankPaymentVerification.status == "Paid")
                      {
                          _logger.LogInformation($"Payment confirmed, updating order {payment.OrderId}");

                          //UPDATE
                          server.order.Order orderToUpdate = _orderReader.GetOrderById(payment.OrderId);
                          orderToUpdate.PurchaseTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
                          orderToUpdate.TransactionId = payment.TransactionId;

                          _logger.LogInformation($"Updating order with Id: {orderToUpdate.Id}");

                          await _orderDataWriter.SendAsync(
                           new UpdateNewOrderCommand(
                      orderToUpdate
                )
            );
                          _logger.LogInformation("Order updated");

                          await jobClient
.NewCompleteJobCommand(job.Key)
.Send();
                          _logger.LogInformation("Verify Payment Job completed");

                          await _zeebeClient
                                .NewPublishMessageCommand()
                                .MessageName("OrderPayedConfirmed")
                                .CorrelationKey(payment.OrderId.ToString())
                                .Variables(JsonConvert.SerializeObject(new
                                {
                                    DeliveryTimeFeel = ConvertToFeelFormat(orderToUpdate.DeliveryTime)
                                }))
                                .Send();
                          _logger.LogInformation("OrderPayed Message sent...");
                      }
                      else
                      {
                          localResponse.Message = "an error occured during token verification";
                          await _zeebeClient
                          .NewThrowErrorCommand(job.Key)
                          .ErrorCode("500")
                          .ErrorMessage(localResponse.Message)
                          .Send();

                          _logger.LogInformation($"An error occurred during the token validation. Status{bankPaymentVerification.status}, code :{bankPaymentVerification.code}");

                      }



                      localResponse.Message = "OK";

                      tcs.TrySetResult(localResponse);
                  }
                  catch (Exception ex)
                  {
                      localResponse.Message = $"Exception in handler: {ex.Message}";
                      _logger.LogInformation($"An Exception occurred: {ex.Message}");
                      tcs.TrySetException(ex);
                  }


              })
              .MaxJobsActive(1)
              .Name(Environment.MachineName)
              .PollInterval(TimeSpan.FromSeconds(1))
              .Timeout(TimeSpan.FromSeconds(30))
              .Open();


    }


    public async Task<GeneralResponse> catchTimeout(int timeIntervalInSeconds = 15)
    {
        timeIntervalInSeconds = timeIntervalInSeconds * 1000;


        await Task.Delay(timeIntervalInSeconds);
        return new GeneralResponse { Message = "TIMEOUT" };
    }

    public string NormalizeTime(string time)
    {


        bool canBeParsed = TimeSpan.TryParse(time, out TimeSpan timespan);
        if (canBeParsed)
        {
            TimeSpan timesn = TimeSpan.Parse(time);
            var deliveryTime = DateTime.Today.Add(timesn);
            return deliveryTime.ToString("yyyy-MM-dd HH:mm");
        }
        else
        {
            return time;
        }

    }

    public string ConvertToFeelFormat(string input)
    {

        DateTime dt = DateTime.ParseExact(input, "yyyy-MM-dd HH:mm", CultureInfo.InvariantCulture);
        string feelDateTime = dt.ToString("s") + "Z"; // "2025-07-18T21:45:00"
        return feelDateTime;
    }




}

