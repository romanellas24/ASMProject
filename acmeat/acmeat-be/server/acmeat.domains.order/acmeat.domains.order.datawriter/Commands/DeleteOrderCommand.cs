using System;

namespace acmeat.server.order.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeleteNewOrderCommand : ICommand
{

    public DeleteNewOrderCommand(Order order){
        this.Id = Guid.NewGuid();
        this.order =order;
    }

    public DeleteNewOrderCommand(int OrderId){
        this.Id = Guid.NewGuid();
        this.OrderId =OrderId;
    }
    public Guid Id {get;set;}
    public int OrderId{get;set;}
    public Order? order{get;set;}
}