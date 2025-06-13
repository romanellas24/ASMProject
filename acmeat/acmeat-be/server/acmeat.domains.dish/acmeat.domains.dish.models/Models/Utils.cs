

using System.Collections.Generic;
using System.Linq;
namespace acmeat.server.dish;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public static class Utils{

    public static Dish ConvertDbElementToServerElement(db.dish.Dish dish){
        return new Dish(dish);
    }


    public static List<Dish> ConvertDbListToServerList(List<db.dish.Dish> dishs){
          return dishs.Select(ConvertDbElementToServerElement).ToList();
    }

    public static db.dish.Dish ConvertServerElementIntoDbELement(Dish dish){
        db.dish.Dish dishDB = new db.dish.Dish(dish.Id,dish.Name,dish.Description,dish.Price,dish.Price,dish.Date);

        dishDB.Id = dish.Id;

        return dishDB;
    }
}