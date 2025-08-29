using System;

namespace acmeat.server.dish.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class UpdateNewDishCommand : ICommand
{

    public UpdateNewDishCommand(dish.Dish dish){
        this.Id = Guid.NewGuid();
        this.dish =dish;
    }


    public Guid Id {get;set;}
    public dish.Dish dish{get;set;}
}