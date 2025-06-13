using System.Collections.Generic;
using acmeat.server.dish;

namespace acmeat.server.dish.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public interface DishDao
  {
    public Dish GetDishById(int DishId);
    public List<Dish> GetDishsByMenuId(int MenuId);
  public List<Dish> GetDishsByDate(string date);
    public List<Dish> GetDishs();


  }