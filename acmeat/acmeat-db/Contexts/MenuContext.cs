
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.menu;
public class MenuContext(MySqlContext context)
{

    private readonly MySqlContext _context = context;

      public List<Menu> GetMenus()
    {
        //  _logger.LogInformation("Getting Menus");
        List<Menu> Menus = _context.MENU
        .ToList();

        return Menus;
    }

    public Menu GetMenuById(int id){
        Menu Menu = _context.MENU
        .Where( (Menu) => Menu.Id == id)
        .AsEnumerable()
        .First();

        return Menu;
    }

    public async Task CreateMenu(Menu Menu){
        await _context.MENU.AddAsync(Menu);
        await _context.SaveChangesAsync();
    }  


    public async Task UpdateMenu(Menu Menu){
        _context.MENU.Update(Menu);
        await _context.SaveChangesAsync();
    }

    
    public async Task DeleteMenu(Menu Menu){
        _context.MENU.Remove(Menu);
        await _context.SaveChangesAsync();
    }
    public async Task DeleteMenuById(int id)
    {
        Menu MenuToDelete = new Menu() { Id = id };
        _context.Entry(MenuToDelete).State = EntityState.Deleted;
        await _context.SaveChangesAsync();
    }
}