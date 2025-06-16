using System;

namespace acmeat.server.menu.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeleteNewMenuCommand : ICommand
{

    public DeleteNewMenuCommand(Menu menu){
        this.Id = Guid.NewGuid();
        this.menu =menu;
    }

    public DeleteNewMenuCommand(int MenuId){
        this.Id = Guid.NewGuid();
        this.MenuId =MenuId;
    }
    public Guid Id {get;set;}
    public int MenuId{get;set;}
    public Menu? menu{get;set;}
}