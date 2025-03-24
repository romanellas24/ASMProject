

using Microsoft.EntityFrameworkCore;

namespace acmeat.db.user;

public class UserContext(MySqlContext context)
{

    private readonly MySqlContext _context = context;

    public List<User> GetUsers()
    {
        //  _logger.LogInformation("Getting users");
        List<User> users = _context.UTENTE
        .ToList();

        return users;
    }

    public User GetUserById(int id)
    {
        User user = _context.UTENTE
        .Where((user) => user.Id == id)
        .AsEnumerable()
        .First();

        return user;
    }

    public async Task CreateUser(User user)
    {
        await _context.UTENTE.AddAsync(user);
        await _context.SaveChangesAsync();
    }


    public async Task UpdateUser(User user)
    {
        _context.UTENTE.Update(user);
        await _context.SaveChangesAsync();
    }


    public async Task DeleteUser(User user)
    {
        _context.UTENTE.Remove(user);
        await _context.SaveChangesAsync();
    }

    public async Task DeleteUserById(int id)
    {
        User UserToDelete = new User() { Id = id };
        _context.Entry(UserToDelete).State = EntityState.Deleted;
        await _context.SaveChangesAsync();
    }
}