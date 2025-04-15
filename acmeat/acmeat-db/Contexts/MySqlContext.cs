using acmeat.db.DeliveryCompany;
using acmeat.db.local;
using acmeat.db.menu;
using acmeat.db.order;
using acmeat.db.user;
using Microsoft.EntityFrameworkCore;

public class MySqlContext : DbContext
{
   public string connectionString = "Server=localhost; User ID=root; Password=pass; Database=blog";

    public DbSet<User> UTENTE { get; set; }
    public DbSet<Order> ORDINE { get; set; }
    public DbSet<DeliveryCompany> SOCIETÀ_CONSEGNA { get; set; }
    public DbSet<Menu> MENU { get; set; }
    public DbSet<Local> LOCALE { get; set; }
    



           // requires using Microsoft.Extensions.Configuration;
    private readonly IConfiguration _configuration;

    public MySqlContext(IConfiguration configuration)
    {
        _configuration = configuration;
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        connectionString = _configuration["DbConnectionString:connectionString"];
        if(connectionString != null)
            optionsBuilder.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
        else
            throw new Exception($"connection string: {connectionString} is evaluated as null");
    }
}

