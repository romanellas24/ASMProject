

using Microsoft.EntityFrameworkCore;

namespace acmeat.db.user;

public class UserContext:MySqlContext
{


    public UserContext(IConfiguration configuration) : base(configuration)
    {
    }

    public List<User> GetUsers()
    {
        //  _logger.LogInformation("Getting users");
        List<User> users = UTENTE
        .ToList();

        return users;
    }
     //fix update -> https://stackoverflow.com/questions/48202403/instance-of-entity-type-cannot-be-tracked-because-another-instance-with-same-key
    public User GetUserById(int id)
    {
        User user = UTENTE.AsNoTracking()
        .Where((user) => user.Id == id)
        .AsEnumerable()
        .First();

        return user;
    }

    public async Task CreateUser(User user)
    {
        await UTENTE.AddAsync(user);
        await SaveChangesAsync();
    }


    public async Task UpdateUser(User user)
    {
        UTENTE.Update(user);
        await SaveChangesAsync();
    }


    public async Task DeleteUser(User user)
    {
        UTENTE.Remove(user);
        await SaveChangesAsync();
    }

    public async Task DeleteUserById(int id)
    {
        User UserToDelete = new User() { Id = id };
        Entry(UserToDelete).State = EntityState.Deleted;
        await SaveChangesAsync();
    }
}