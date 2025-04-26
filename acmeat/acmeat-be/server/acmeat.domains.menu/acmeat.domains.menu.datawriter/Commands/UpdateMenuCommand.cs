using System;

namespace acmeat.server.menu.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class UpdateNewMenuCommand : ICommand
{

    public UpdateNewMenuCommand(menu.Menu menu){
        this.Id = Guid.NewGuid();
        this.menu =menu;
    }


    public Guid Id {get;set;}
    public menu.Menu menu{get;set;}
}