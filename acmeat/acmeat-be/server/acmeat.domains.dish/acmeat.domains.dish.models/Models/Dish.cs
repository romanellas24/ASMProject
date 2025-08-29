

using System;
using System.Text.Json.Serialization;

namespace acmeat.server.dish;
//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class Dish
{

    [JsonConstructor]
    public Dish(
              int Id, string Name, string Description, float Price, int MenuId
               )
    {
        this.Id = Id;
        this.Description = Description;
        this.Name = Name;
        this.Price = Price;
        this.MenuId = MenuId;

    }

    public Dish(db.dish.Dish dish)
    {
        this.Id = dish.Id;
        this.Description = dish.Description;
        this.Name = dish.Name;
        this.Price = dish.Price;
        this.MenuId = dish.MenuId;

    }

    public int Id { get; set; }

    public string Description { get; set; }
    public string Name { get; set; }


    public float Price { get; set; }

    public int MenuId { get; set; }


    // public string Date { get; set; }

    public acmeat.db.dish.Dish Convert()
    {
        acmeat.db.dish.Dish dish = new acmeat.db.dish.Dish();
        dish.Id = this.Id;
        dish.Description = this.Description;
        dish.Name = this.Name;
        dish.Price = this.Price;
        dish.MenuId = this.MenuId;
        return dish;
    }
}