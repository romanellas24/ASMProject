

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;


namespace acmeat.server.local.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class LocalReader :LocalDao{

    private readonly  MysqlClient _mysqlClient;
    private readonly ILogger<LocalReader> _logger;
    private readonly DbConnectionOptions _options;

    public LocalReader(
        ILogger<LocalReader> logger,
        MysqlClient mysqlClient,
        IOptions<DbConnectionOptions> options){
        _options = options.Value;
        _logger = logger;    
        _mysqlClient = mysqlClient;


        // _logger.LogInformation($"Configuration taken, connection to db:{_options.connectionString}");
    }
    public List<acmeat.server.local.Local>GetLocals(){
        _logger.LogInformation($"Getting Locals");
       List<acmeat.db.local.Local> locals= _mysqlClient.GetLocals();
        return Utils.ConvertDbListToServerList(locals); 
       
    }
    
    public Local GetLocalById(int id){
        _logger.LogInformation($"Getting Local with id: {id}");
      acmeat.db.local.Local local= _mysqlClient.GetLocalById(id);
        return Utils.ConvertDbElementToServerElement(local); 
       
    }
}