


using acmeat.server.dish.client;
using acmeat.server.menu.client;

namespace acmeat.api.local;

public class DailyUpdate
{

    public DailyUpdate(string localUrl, List<Dish> dishes, string Date)
    {
        this.LocalUrl = localUrl;
        // this.Menu = menu;
        this.dishes = dishes;
        this.Date = Date;

    }

    public string LocalUrl { get; set; }
    public List<Dish> dishes { get; set; }
    
    public string Date { get; set; }

}