
using System.Text.Json;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;

namespace acmeat.db.mysql
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class MysqlClient : ControllerBase
    {

         // requires using Microsoft.Extensions.Configuration;
    private readonly IConfiguration _configuration;
    private readonly ILogger<MysqlClient> _logger;

    public MysqlClient(IConfiguration configuration,ILogger<MysqlClient> logger)
    {
        _configuration = configuration;
        _logger = logger;
    }

    [HttpGet]
    public async Task GetSomething(){
        string DbConnectionString = _configuration["DbConnectionString:connectionString"];

        _logger.LogInformation($"Connection string for db : {JsonSerializer.Serialize(DbConnectionString)}");
    }


    }
}
