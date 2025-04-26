

using System.Text.Json.Serialization;

namespace acmeat.server.menu;
//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class Menu
{

    [JsonConstructor]
    public Menu(
               int Id,string Descritpion, string Type, int Price
               )
    {
        this.Id = Id;
        this.Descritpion=Descritpion;
        this.Type = Type;
        this.Price = Price;


    }

    public Menu(db.menu.Menu menu)
    {
      this.Id = menu.Id;
        this.Descritpion=menu.Descritpion;
        this.Type = menu.Type;
        this.Price = menu.Price;

    }
    public int Id { get; set; }

    public string Descritpion { get; set; }
    public string Type { get; set; }


    public int Price { get; set; }

    public acmeat.db.menu.Menu Convert()
    {
        acmeat.db.menu.Menu menu = new acmeat.db.menu.Menu();
        menu.Id = this.Id;
        menu.Descritpion =  this.Descritpion;
        menu.Type = this.Type;
        menu.Price = this.Price ;
        return menu;
    }
}