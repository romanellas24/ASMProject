using System.Threading.Tasks;
using acmeat.server.menu.manager;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.menu.dataproxy;
using acmeat.server.menu.datawriter;
using acmeat.server.menu;
using System.Collections.Generic;
using Google.Protobuf;
using System.Linq;
using System;
using Microsoft.Azure.Cosmos.Linq;

namespace acmeat.domains.menu.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcMenuManagerService : server.menu.manager.GrpcMenu.GrpcMenuBase
{
    private readonly ILogger<GrpcMenuManagerService> _logger;
    private MenuReader _menuReader;
    private  MenuDataWriter _menuDataWriter;
    public GrpcMenuManagerService(
        ILogger<GrpcMenuManagerService> logger,
        MenuReader menuReader,
        MenuDataWriter menuDataWriter)
    {
        _logger = logger;
        _menuDataWriter = menuDataWriter;
        _menuReader = menuReader;
    }

    public override Task<server.menu.manager.HelloReplyClient> SayHello(server.menu.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.menu.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.menu.manager.Menu> GetMenuById(Id id, ServerCallContext context)
    {
        return Task.FromResult(
            
           new server.menu.manager.Menu(ConvertServerModelToGrpc(_menuReader.GetMenuById(id.Id_)))
        );
    }

     public override Task<server.menu.manager.MenuList> GetMenus(Id id, ServerCallContext context)
    {
        List<server.menu.Menu> menus=  _menuReader.GetMenus();
        MenuList menuList =  new server.menu.manager.MenuList();

        menuList.Menus.AddRange(ConvertServerListToGrpc(menus));

        return Task.FromResult(
            menuList
          
        );
    }

       public override async Task<GeneralResponse> CreateMenu(server.menu.manager.Menu menu, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _menuDataWriter.SendAsync(
            new CreateNewMenuCommand(
                ConvertGrpcToServerModel(menu)
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

       public override async Task<GeneralResponse> UpdateMenu(server.menu.manager.Menu menu, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _menuDataWriter.SendAsync(
            new UpdateNewMenuCommand(
                ConvertGrpcToServerModel(menu)
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

       public override async Task<GeneralResponse> DeleteMenu(server.menu.manager.Menu menu, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _menuDataWriter.SendAsync(
            new DeleteNewMenuCommand(
                menu.Id
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


    public List<server.menu.manager.Menu> ConvertServerListToGrpc(List<server.menu.Menu> menus){
        return menus.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.menu.Menu ConvertGrpcToServerModel(server.menu.manager.Menu menu){
        return new server.menu.Menu(menu.Id,menu.Description,menu.Type,menu.Price );
    }


    public server.menu.manager.Menu ConvertServerModelToGrpc(server.menu.Menu menu){
        var menut =  new server.menu.manager.Menu();
        menut.Id = menu.Id;
        menut.Description = menu.Descritpion;
        menut.Type = menu.Type;
        menut.Price = menu.Price;
        return menut;

    }
}
