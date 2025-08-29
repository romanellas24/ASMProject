

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;


namespace acmeat.server.dish.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DishReader : DishDao
{

    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<DishReader> _logger;
    private readonly DbConnectionOptions _options;

    public DishReader(
        ILogger<DishReader> logger,
        MysqlClient mysqlClient,
        IOptions<DbConnectionOptions> options)
    {
        _options = options.Value;
        _logger = logger;
        _mysqlClient = mysqlClient;


        // _logger.LogInformation($"Configuration taken, connection to db:{_options.connectionString}");
    }
    public List<acmeat.server.dish.Dish> GetDishs()
    {
        _logger.LogInformation($"Getting Dishs");
        List<acmeat.db.dish.Dish> dishs = _mysqlClient.GetDishs();
        return Utils.ConvertDbListToServerList(dishs);

    }

    public Dish GetDishById(int id)
    {
        _logger.LogInformation($"Getting Dish with id: {id}");
        acmeat.db.dish.Dish dish = _mysqlClient.GetDishById(id);
        return Utils.ConvertDbElementToServerElement(dish);

    }

    public List<acmeat.server.dish.Dish> GetDishsByMenuId(int MenuId)
    {
        _logger.LogInformation($"Getting Dishs from Local with id : {MenuId}");
        List<acmeat.db.dish.Dish> dishs = _mysqlClient.GetDishsByMenuId(MenuId);
        return Utils.ConvertDbListToServerList(dishs);

    }
    
}