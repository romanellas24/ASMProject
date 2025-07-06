using System.Threading.Tasks;
using acmeat.server.dish.manager;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.dish.dataproxy;
using acmeat.server.dish.datawriter;
using acmeat.server.dish;
using System.Collections.Generic;
using Google.Protobuf;
using System.Linq;
using System;
using Microsoft.Azure.Cosmos.Linq;

namespace acmeat.domains.dish.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcDishManagerService : server.dish.manager.GrpcDish.GrpcDishBase
{
    private readonly ILogger<GrpcDishManagerService> _logger;
    private DishReader _dishReader;
    private DishDataWriter _dishDataWriter;
    private readonly int deadLineHour = 10;
    public GrpcDishManagerService(
        ILogger<GrpcDishManagerService> logger,
        DishReader dishReader,
        DishDataWriter dishDataWriter)
    {
        _logger = logger;
        _dishDataWriter = dishDataWriter;
        _dishReader = dishReader;
    }

    public override Task<server.dish.manager.HelloReplyClient> SayHello(server.dish.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.dish.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.dish.manager.Dish> GetDishById(Id id, ServerCallContext context)
    {
        return Task.FromResult(

           new server.dish.manager.Dish(ConvertServerModelToGrpc(_dishReader.GetDishById(id.Id_)))
        );
    }

    public override Task<server.dish.manager.DishList> GetDishsByMenuId(Id id, ServerCallContext context)
    {

        List<server.dish.Dish> dishs = _dishReader.GetDishsByMenuId(id.Id_);
        DishList dishList = new server.dish.manager.DishList();

        dishList.Dishs.AddRange(ConvertServerListToGrpc(dishs));

        return Task.FromResult(
            dishList
          );
    }

    public override Task<server.dish.manager.DishList> GetDishs(Id id, ServerCallContext context)
    {
        List<server.dish.Dish> dishs = _dishReader.GetDishs();
        DishList dishList = new server.dish.manager.DishList();

        dishList.Dishs.AddRange(ConvertServerListToGrpc(dishs));

        return Task.FromResult(
            dishList

        );
    }

    public override async Task<GeneralResponse> CreateDish(server.dish.manager.Dish dish, ServerCallContext context)
    {
        GeneralResponse generalResponse = new GeneralResponse();
        try
        {
            await _dishDataWriter.SendAsync(
             new CreateNewDishCommand(
                 ConvertGrpcToServerModel(dish)
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

    public override async Task<GeneralResponse> UpdateDish(server.dish.manager.Dish dish, ServerCallContext context)
    {
        GeneralResponse generalResponse = new GeneralResponse();
        try
        {
            await _dishDataWriter.SendAsync(
             new UpdateNewDishCommand(
                 ConvertGrpcToServerModel(dish)
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

    public override async Task<GeneralResponse> UpdateDishs(DishList dishList, ServerCallContext context)
    {
        GeneralResponse generalResponse = new GeneralResponse();
        _logger.LogInformation($"Updating Dishs for Local with Id: {dishList.Dishs.First().MenuId}");
        //https://learn.microsoft.com/en-us/dotnet/standard/datetime/how-to-use-dateonly-timeonly
        TimeOnly timeOnly = TimeOnly.FromDateTime(DateTime.Now);
        _logger.LogInformation($"Current time {timeOnly.Hour}:{timeOnly.Minute}");

        if (timeOnly.Hour > deadLineHour)
        {
            _logger.LogInformation($"Cannot update the dishs its too late...");
            // GeneralResponse response = new GeneralResponse();
            throw new Exception($"Cannot update the dishs its too late...");
        }
        else
        {
            try
            {

                foreach (server.dish.manager.Dish dish in dishList.Dishs)
                {
                    await _dishDataWriter.SendAsync(
            new UpdateNewDishCommand(
                ConvertGrpcToServerModel(dish)
                )
            );
                }

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

    }

    public override async Task<GeneralResponse> DeleteDish(server.dish.manager.Dish dish, ServerCallContext context)
    {
        GeneralResponse generalResponse = new GeneralResponse();
        try
        {
            await _dishDataWriter.SendAsync(
             new DeleteNewDishCommand(
                 dish.Id
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


    public List<server.dish.manager.Dish> ConvertServerListToGrpc(List<server.dish.Dish> dishs)
    {
        return dishs.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.dish.Dish ConvertGrpcToServerModel(server.dish.manager.Dish dish)
    {
        return new server.dish.Dish(dish.Id,dish.Name,dish.Description,dish.Price,dish.Price);
    }


    public server.dish.manager.Dish ConvertServerModelToGrpc(server.dish.Dish dish)
    {
        var disht = new server.dish.manager.Dish();
        disht.Id = dish.Id;
        disht.Description = dish.Description;
        disht.Name = dish.Name;
        disht.Price = dish.Price;
        disht.MenuId = dish.MenuId;
        return disht;

    }
}
