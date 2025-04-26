

using System.Collections.Generic;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;


namespace acmeat.server.deliverycompany.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeliveryCompanyReader :DeliveryCompanyDao{

    private readonly  MysqlClient _mysqlClient;
    private readonly ILogger<DeliveryCompanyReader> _logger;
    private readonly DbConnectionOptions _options;

    public DeliveryCompanyReader(
        ILogger<DeliveryCompanyReader> logger,
        MysqlClient mysqlClient,
        IOptions<DbConnectionOptions> options){
        _options = options.Value;
        _logger = logger;    
        _mysqlClient = mysqlClient;


        // _logger.LogInformation($"Configuration taken, connection to db:{_options.connectionString}");
    }
    public List<acmeat.server.deliverycompany.DeliveryCompany>GetDeliveryCompanys(){
        _logger.LogInformation($"Getting DeliveryCompanys");
       List<acmeat.db.deliveryCompany.DeliveryCompany> deliverycompanys= _mysqlClient.GetDeliveryCompanys();
        return Utils.ConvertDbListToServerList(deliverycompanys); 
       
    }
    
    public DeliveryCompany GetDeliveryCompanyById(int id){
        _logger.LogInformation($"Getting DeliveryCompany with id: {id}");
      acmeat.db.deliveryCompany.DeliveryCompany deliverycompany= _mysqlClient.GetDeliveryCompanyById(id);
        return Utils.ConvertDbElementToServerElement(deliverycompany); 
       
    }
}