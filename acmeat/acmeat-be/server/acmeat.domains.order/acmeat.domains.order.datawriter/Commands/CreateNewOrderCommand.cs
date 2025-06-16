using System;

namespace acmeat.server.order.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class CreateNewOrderCommand : ICommand
{

    public CreateNewOrderCommand(acmeat.server.order.Order order){
        this.Id = Guid.NewGuid();
        this.order =order;
    }
    public Guid Id {get;set;}
    public acmeat.server.order.Order order{get;set;}
}