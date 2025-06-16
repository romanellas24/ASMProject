using System;

namespace acmeat.server.user.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class UpdateNewUserCommand : ICommand
{

    public UpdateNewUserCommand(user.User user){
        this.Id = Guid.NewGuid();
        this.user =user;
    }


    public Guid Id {get;set;}
    public user.User user{get;set;}
}