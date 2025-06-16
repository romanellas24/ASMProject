using System.Threading.Tasks;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
namespace acmeat.server.local.datawriter;



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class LocalDataWriter :
ICommandHandler<CreateNewLocalCommand>,
ICommandHandler<UpdateNewLocalCommand>,
ICommandHandler<DeleteNewLocalCommand>
{
    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<LocalDataWriter> _logger;

    public LocalDataWriter(
        ILogger<LocalDataWriter> logger,
        MysqlClient mysqlClient
    )
    {
        _logger = logger;
        _mysqlClient = mysqlClient;
    }

    public async Task SendAsync(CreateNewLocalCommand command)
    {
        if (command.local != null)
        {
            _logger.LogInformation($"Command: {command.GetType().Name}. LocalId: {command.local.Id}");
            await _mysqlClient.CreateLocal(Utils.ConvertServerElementIntoDbELement(command.local));
            _logger.LogInformation($"Local with id {command.local.Id} has been created");
        }
        else
        {
            throw new System.Exception($"Local is null!");
        }

    }

    public async Task SendAsync(UpdateNewLocalCommand command)
    {
        if (command.local != null)
        {
            var local = _mysqlClient.GetLocalById(command.local.Id);


            if (local != null)
            {
                _logger.LogInformation($"Command: {command.GetType().Name}. LocalId: {command.local.Id}");
                await _mysqlClient.UpdateLocal(command.local.Convert());
                _logger.LogInformation($"Local with id {command.local.Id} has been updated");
            }else{
                throw new System.Exception($"Local {command.local.Id} has not been found");
            }

        }
        else
        {
            throw new System.Exception($"Local is null!");
        }
    }


    public async Task SendAsync(DeleteNewLocalCommand command)
    {

        if (command.local != null)
        {

            var local = _mysqlClient.GetLocalById(command.local.Id);

            if (local != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. LocalId: {command.local.Id}");
                await _mysqlClient.DeleteLocal(local);
                _logger.LogInformation($"Local with id {command.local.Id} has been deleted");

            }
            else
            {
                throw new System.Exception($"Local with {command.local.Id} doesn't exist");
            }


        }else{
            var local = _mysqlClient.GetLocalById(command.LocalId);

            if (local != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. LocalId: {command.LocalId}");
                await _mysqlClient.DeleteLocal(local);
                _logger.LogInformation($"Local with id {command.LocalId} has been deleted");

            }
            else
            {
                throw new System.Exception($"Local with {command.LocalId} doesn't exist");
            }
        }
    }

}
