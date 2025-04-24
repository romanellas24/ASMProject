

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;


namespace acmeat.server.user.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class UserReader :UserDao{

    private readonly  MysqlClient _mysqlClient;
    private readonly ILogger<UserReader> _logger;
    private readonly DbConnectionOptions _options;

    public UserReader(
        ILogger<UserReader> logger,
        MysqlClient mysqlClient,
        IOptions<DbConnectionOptions> options){
        _options = options.Value;
        _logger = logger;    
        _mysqlClient = mysqlClient;


        // _logger.LogInformation($"Configuration taken, connection to db:{_options.connectionString}");
    }
    public List<acmeat.server.user.User>GetUsers(){
        _logger.LogInformation($"Getting Users");
       List<acmeat.db.user.User> users= _mysqlClient.GetUsers();
        return Utils.ConvertDbListToServerList(users); 
       
    }
    
    public User GetUserById(int id){
        _logger.LogInformation($"Getting User with id: {id}");
      acmeat.db.user.User user= _mysqlClient.GetUserById(id);
        return Utils.ConvertDbElementToServerElement(user); 
       
    }
}