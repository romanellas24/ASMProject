
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.local;
public class LocalContext(MySqlContext context)
{

    private readonly MySqlContext _context = context;

        public List<Local> GetLocals()
    {
        //  _logger.LogInformation("Getting Locals");
        List<Local> Locals = _context.LOCALE
        .ToList();

        return Locals;
    }

    public Local GetLocalById(int id){
        Local Local = _context.LOCALE
        .Where( (Local) => Local.Id == id)
        .AsEnumerable()
        .First();

        return Local;
    }

    public async Task CreateLocal(Local Local){
        await _context.LOCALE.AddAsync(Local);
        await _context.SaveChangesAsync();
    }  


    public async Task UpdateLocal(Local Local){
        _context.LOCALE.Update(Local);
        await _context.SaveChangesAsync();
    }

    
    public async Task DeleteLocal(Local Local){
        _context.LOCALE.Remove(Local);
        await _context.SaveChangesAsync();
    }

    public async Task DeleteLocalById(int id)
    {
        Local LocalToDelete = new Local() { Id = id };
        _context.Entry(LocalToDelete).State = EntityState.Deleted;
        await _context.SaveChangesAsync();
    }
}