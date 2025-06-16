using System;

namespace acmeat.server.local.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeleteNewLocalCommand : ICommand
{

    public DeleteNewLocalCommand(Local local){
        this.Id = Guid.NewGuid();
        this.local =local;
    }

    public DeleteNewLocalCommand(int LocalId){
        this.Id = Guid.NewGuid();
        this.LocalId =LocalId;
    }
    public Guid Id {get;set;}
    public int LocalId{get;set;}
    public Local? local{get;set;}
}