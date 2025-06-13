using System;

namespace acmeat.server.dish.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeleteNewDishCommand : ICommand
{

    public DeleteNewDishCommand(Dish dish){
        this.Id = Guid.NewGuid();
        this.dish =dish;
    }

    public DeleteNewDishCommand(int DishId){
        this.Id = Guid.NewGuid();
        this.DishId =DishId;
    }
    public Guid Id {get;set;}
    public int DishId{get;set;}
    public Dish? dish{get;set;}
}