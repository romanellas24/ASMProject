

using System.Collections.Generic;
using System.Linq;
namespace acmeat.server.local;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public static class Utils{

    public static Local ConvertDbElementToServerElement(db.local.Local local){
        return new Local(local);
    }


    public static List<Local> ConvertDbListToServerList(List<db.local.Local> locals){
          return locals.Select(ConvertDbElementToServerElement).ToList();
    }

    public static db.local.Local ConvertServerElementIntoDbELement(Local local){
        db.local.Local localDB = new db.local.Local(local.Id,local.Address,local.OpeningTime,local.ClosingTime,local.Address,local.OpeningDays,local.Available);

        localDB.Id = local.Id;

        return localDB;
    }
}