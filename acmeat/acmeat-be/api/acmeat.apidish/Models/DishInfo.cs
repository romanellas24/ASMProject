using System.Text.Json.Serialization;
using acmeat.server.dish.client;

namespace acmeat.api.dish;
public class DishInfo
{

    [JsonConstructor]
    public DishInfo(
            string Name, string Description, int Price, int MenuId, string Date)
    {
       this.Id = new Random().Next();
        this.Description = Description;
        this.Name = Name;
        this.Price = Price;
        this.MenuId = MenuId;
        this.Date = Date;
    }


    public DishInfo(
            string Name, string Description, int Price, int MenuId)
    {
       this.Id = Id;
        this.Description = Description;
        this.Name = Name;
        this.Price = Price;
        this.MenuId = MenuId;
        this.Date =  DateTime.Now.ToString("yyyy-MM-dd");
    }

    public DishInfo(acmeat.server.dish.client.Dish dish)
    {

        this.Id = dish.Id;
        this.Description = dish.Description;
        this.Name = dish.Name;
        this.Price = dish.Price;
        this.MenuId = dish.MenuId;
        this.Date = dish.Date;

    }

    
    public int Id { get; set; }

    public string Description { get; set; }
    public string Name { get; set; }


    public int Price { get; set; }

    public int MenuId { get; set; }


    public string Date { get; set; }
    public acmeat.server.dish.client.Dish Convert()
    {
        acmeat.server.dish.client.Dish dish = new acmeat.server.dish.client.Dish();
        dish.Id = this.Id;
        dish.Description = this.Description;
        dish.Name = this.Name;
        dish.Price = this.Price;
        dish.MenuId = this.MenuId;
        dish.Date = this.Date;
        return dish;
    }
}