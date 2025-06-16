using System;

namespace acmeat.server.local.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class UpdateNewLocalCommand : ICommand
{

    public UpdateNewLocalCommand(local.Local local){
        this.Id = Guid.NewGuid();
        this.local =local;
    }


    public Guid Id {get;set;}
    public local.Local local{get;set;}
}