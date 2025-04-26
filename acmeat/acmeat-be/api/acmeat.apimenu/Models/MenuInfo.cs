using System.Text.Json.Serialization;

namespace acmeat.api.menu;
public class MenuInfo
{

    [JsonConstructor]
    public MenuInfo(
               string Descritpion, string Type, int Price)
    {
        this.Id = new Random().Next();
        this.Descritpion = Descritpion;
        this.Type = Type;
        this.Price = Price;
    }


    public MenuInfo(acmeat.server.menu.client.Menu menu)
    {

        this.Id = menu.Id;
        this.Descritpion = menu.Description;
        this.Type = menu.Type;
        this.Price = menu.Price;

    }

    public int Id { get; set; }

    public string Descritpion { get; set; }
    public string Type { get; set; }


    public int Price { get; set; }
    public acmeat.server.menu.client.Menu Convert()
    {
        acmeat.server.menu.client.Menu menu = new acmeat.server.menu.client.Menu();
        menu.Id = this.Id;
        menu.Description = this.Descritpion;
        menu.Type = this.Type;
        menu.Price = this.Price;
        return menu;
    }
}