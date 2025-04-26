

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;


namespace acmeat.server.menu.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class MenuReader :MenuDao{

    private readonly  MysqlClient _mysqlClient;
    private readonly ILogger<MenuReader> _logger;
    private readonly DbConnectionOptions _options;

    public MenuReader(
        ILogger<MenuReader> logger,
        MysqlClient mysqlClient,
        IOptions<DbConnectionOptions> options){
        _options = options.Value;
        _logger = logger;    
        _mysqlClient = mysqlClient;


        // _logger.LogInformation($"Configuration taken, connection to db:{_options.connectionString}");
    }
    public List<acmeat.server.menu.Menu>GetMenus(){
        _logger.LogInformation($"Getting Menus");
       List<acmeat.db.menu.Menu> menus= _mysqlClient.GetMenus();
        return Utils.ConvertDbListToServerList(menus); 
       
    }
    
    public Menu GetMenuById(int id){
        _logger.LogInformation($"Getting Menu with id: {id}");
      acmeat.db.menu.Menu menu= _mysqlClient.GetMenuById(id);
        return Utils.ConvertDbElementToServerElement(menu); 
       
    }
}