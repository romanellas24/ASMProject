

using System.Collections.Generic;
using System.Linq;
namespace acmeat.server.menu;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public static class Utils{

    public static Menu ConvertDbElementToServerElement(db.menu.Menu menu){
        return new Menu(menu);
    }


    public static List<Menu> ConvertDbListToServerList(List<db.menu.Menu> menus){
          return menus.Select(ConvertDbElementToServerElement).ToList();
    }

    public static db.menu.Menu ConvertServerElementIntoDbELement(Menu menu){
        db.menu.Menu menuDB = new db.menu.Menu(menu.Id,menu.Descritpion,menu.Type,menu.Price);

        menuDB.Id = menu.Id;

        return menuDB;
    }
}