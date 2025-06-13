


using acmeat.server.dish.client;
using acmeat.server.menu.client;

namespace acmeat.api.local;

public class DailyUpdate
{

    public DailyUpdate(string localUrl, List<Dish> dishes)
    {
        this.LocalUrl = localUrl;
        // this.Menu = menu;
        this.dishes = dishes;
        
    }

    public string LocalUrl { get; set; }
    public List<Dish> dishes { get; set; }

}