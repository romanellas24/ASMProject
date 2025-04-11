
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.local;
public class LocalContext:MySqlContext
{


    public LocalContext(IConfiguration configuration) : base(configuration)
    {
    }

    public List<Local> GetLocals()
    {
        //  _logger.LogInformation("Getting Locals");
        List<Local> Locals = LOCALE
        .ToList();

        return Locals;
    }
     //fix update -> https://stackoverflow.com/questions/48202403/instance-of-entity-type-cannot-be-tracked-because-another-instance-with-same-key
    public Local GetLocalById(int id){
        Local Local = LOCALE.AsNoTracking()
        .Where( (Local) => Local.Id == id)
        .AsEnumerable()
        .First();

        return Local;
    }

    public async Task CreateLocal(Local Local){
        await LOCALE.AddAsync(Local);
        await SaveChangesAsync();
    }  


    public async Task UpdateLocal(Local Local){
        LOCALE.Update(Local);
        await SaveChangesAsync();
    }

    
    public async Task DeleteLocal(Local Local){
        LOCALE.Remove(Local);
        await SaveChangesAsync();
    }

    public async Task DeleteLocalById(int id)
    {
        Local LocalToDelete = new Local() { Id = id };
        Entry(LocalToDelete).State = EntityState.Deleted;
        await SaveChangesAsync();
    }
}