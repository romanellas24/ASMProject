using System;

namespace acmeat.server.user.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeleteNewUserCommand : ICommand
{

    public DeleteNewUserCommand(User user){
        this.Id = Guid.NewGuid();
        this.user =user;
    }

    public DeleteNewUserCommand(int UserId){
        this.Id = Guid.NewGuid();
        this.UserId =UserId;
    }
    public Guid Id {get;set;}
    public int UserId{get;set;}
    public User? user{get;set;}
}