
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.menu;
public class MenuContext:MySqlContext
{


    public MenuContext(IConfiguration configuration) : base(configuration)
    {
    }

    public List<Menu> GetMenus()
    {
        //  _logger.LogInformation("Getting Menus");
        List<Menu> Menus = MENU
        .ToList();

        return Menus;
    }

     //fix update -> https://stackoverflow.com/questions/48202403/instance-of-entity-type-cannot-be-tracked-because-another-instance-with-same-key
    public Menu GetMenuById(int id){
        Menu Menu = MENU.AsNoTracking()
        .Where( (Menu) => Menu.Id == id)
        .AsEnumerable()
        .First();

        return Menu;
    }

    public List<Menu> GetMenusByLocalId(int LocalId){
        return MENU.Where(menu => menu.LocalId == LocalId).ToList();
    }

    public async Task CreateMenu(Menu Menu){
        await MENU.AddAsync(Menu);
        await SaveChangesAsync();
    }  


    public async Task UpdateMenu(Menu Menu){
        MENU.Update(Menu);
        await SaveChangesAsync();
    }

    
    public async Task DeleteMenu(Menu Menu){
        MENU.Remove(Menu);
        await SaveChangesAsync();
    }
    public async Task DeleteMenuById(int id)
    {
        Menu MenuToDelete = new Menu() { Id = id };
        Entry(MenuToDelete).State = EntityState.Deleted;
        await SaveChangesAsync();
    }
}