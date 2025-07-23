
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.dish;
public class DishContext:MySqlContext
{


    public DishContext(IConfiguration configuration) : base(configuration)
    {
    }

    public List<Dish> GetDishs()
    {
        //  _logger.LogInformation("Getting Dishs");
        List<Dish> Dishs = PIATTO
        .ToList();

        return Dishs;
    }

     //fix update -> https://stackoverflow.com/questions/48202403/instance-of-entity-type-cannot-be-tracked-because-another-instance-with-same-key
    public Dish GetDishById(int id){
        Dish Dish = PIATTO.AsNoTracking()
        .Where( (Dish) => Dish.Id == id)
        .AsEnumerable()
        .First();

        return Dish;
    }

    public List<Dish> GetDishsByMenuId(int MenuId){
        return PIATTO.Where(dish => dish.MenuId == MenuId).ToList();
    }
    

    public async Task CreateDish(Dish Dish)
    {
        await PIATTO.AddAsync(Dish);
        await SaveChangesAsync();
    }  


    public async Task UpdateDish(Dish Dish){
        PIATTO.Update(Dish);
        await SaveChangesAsync();
    }

    
    public async Task DeleteDish(Dish Dish){
        PIATTO.Remove(Dish);
        await SaveChangesAsync();
    }
    public async Task DeleteDishById(int id)
    {
        Dish DishToDelete = new Dish() { Id = id };
        Entry(DishToDelete).State = EntityState.Deleted;
        await SaveChangesAsync();
    }
}