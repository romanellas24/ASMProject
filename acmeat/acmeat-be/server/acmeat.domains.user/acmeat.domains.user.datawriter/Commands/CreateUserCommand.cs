using System;

namespace acmeat.server.user.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class CreateNewUserCommand : ICommand
{

    public CreateNewUserCommand(User user){
        this.Id = Guid.NewGuid();
        this.user =user;
    }
    public Guid Id {get;set;}
    public User user {get;set;}
}