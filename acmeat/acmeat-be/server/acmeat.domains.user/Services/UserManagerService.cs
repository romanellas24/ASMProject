using System.Threading.Tasks;
using acmeat.server.user.manager;
using Grpc.Core;
using Microsoft.Extensions.Logging;
using acmeat.server.user.dataproxy;
using acmeat.server.user.datawriter;
using acmeat.server.user;
using System.Collections.Generic;
using Google.Protobuf;
using System.Linq;
using System;

namespace acmeat.domains.user.Services;


//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

public class GrpcUserManagerService : server.user.manager.GrpcUser.GrpcUserBase
{
    private readonly ILogger<GrpcUserManagerService> _logger;
    private UserReader _userReader;
    private  UserDataWriter _userDataWriter;
    public GrpcUserManagerService(
        ILogger<GrpcUserManagerService> logger,
        UserReader userReader,
        UserDataWriter userDataWriter)
    {
        _logger = logger;
        _userDataWriter = userDataWriter;
        _userReader = userReader;
    }

    public override Task<server.user.manager.HelloReplyClient> SayHello(server.user.manager.HelloRequestClient request, ServerCallContext context)
    {
        return Task.FromResult(new server.user.manager.HelloReplyClient
        {
            Message = "Hello " + request.Name
        });
    }

    public override Task<server.user.manager.User> GetUserById(Id id, ServerCallContext context)
    {
        return Task.FromResult(
            
           new server.user.manager.User(ConvertServerModelToGrpc(_userReader.GetUserById(id.Id_)))
        );
    }

     public override Task<server.user.manager.UserList> GetUsers(Id id, ServerCallContext context)
    {
        List<server.user.User> users=  _userReader.GetUsers();
        UserList userList =  new server.user.manager.UserList();

        userList.Users.AddRange(ConvertServerListToGrpc(users));

        return Task.FromResult(
            userList
          
        );
    }

       public override async Task<GeneralResponse> CreateUser(server.user.manager.User user, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _userDataWriter.SendAsync(
            new CreateNewUserCommand(
                ConvertGrpcToServerModel(user)
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

       public override async Task<GeneralResponse> UpdateUser(server.user.manager.User user, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _userDataWriter.SendAsync(
            new UpdateNewUserCommand(
                ConvertGrpcToServerModel(user)
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

       public override async Task<GeneralResponse> DeleteUser(server.user.manager.User user, ServerCallContext context)
    {
         GeneralResponse generalResponse = new GeneralResponse();
        try{
           await _userDataWriter.SendAsync(
            new DeleteNewUserCommand(
                user.Id
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


    public List<server.user.manager.User> ConvertServerListToGrpc(List<server.user.User> users){
        return users.Select(ConvertServerModelToGrpc).ToList();
    }

    public server.user.User ConvertGrpcToServerModel(server.user.manager.User user){
        return new server.user.User(user.Id,user.Address,user.Mail,user.Pwd );
    }


    public server.user.manager.User ConvertServerModelToGrpc(server.user.User user){
        var usert =  new server.user.manager.User();
        usert.Id = user.Id;
        usert.Address = user.Address;
        usert.Mail = user.Mail;
        usert.Pwd = user.Pwd;
        return usert;

    }
}
