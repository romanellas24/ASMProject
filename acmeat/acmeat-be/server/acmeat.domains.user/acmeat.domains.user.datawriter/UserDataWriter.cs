using System.Threading.Tasks;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
namespace acmeat.server.user.datawriter;



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class UserDataWriter :
ICommandHandler<CreateNewUserCommand>,
ICommandHandler<UpdateNewUserCommand>,
ICommandHandler<DeleteNewUserCommand>
{
    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<UserDataWriter> _logger;

    public UserDataWriter(
        ILogger<UserDataWriter> logger,
        MysqlClient mysqlClient
    )
    {
        _logger = logger;
        _mysqlClient = mysqlClient;
    }

    public async Task SendAsync(CreateNewUserCommand command)
    {
        if (command.user != null)
        {
            _logger.LogInformation($"Command: {command.GetType().Name}. UserId: {command.user.Id}");
            await _mysqlClient.CreateUser(Utils.ConvertServerElementIntoDbELement(command.user));
            _logger.LogInformation($"User with id {command.user.Id} has been created");
        }
        else
        {
            throw new System.Exception($"User is null!");
        }

    }

    public async Task SendAsync(UpdateNewUserCommand command)
    {
        if (command.user != null)
        {
            var user = _mysqlClient.GetUserById(command.user.Id);


            if (user != null)
            {
                _logger.LogInformation($"Command: {command.GetType().Name}. UserId: {command.user.Id}");
                await _mysqlClient.UpdateUser(command.user.Convert());
                _logger.LogInformation($"User with id {command.user.Id} has been updated");
            }else{
                throw new System.Exception($"User {command.user.Id} has not been found");
            }

        }
        else
        {
            throw new System.Exception($"User is null!");
        }
    }


    public async Task SendAsync(DeleteNewUserCommand command)
    {

        if (command.user != null)
        {

            var user = _mysqlClient.GetUserById(command.user.Id);

            if (user != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. UserId: {command.user.Id}");
                await _mysqlClient.DeleteUser(user);
                _logger.LogInformation($"User with id {command.user.Id} has been deleted");

            }
            else
            {
                throw new System.Exception($"User with {command.user.Id} doesn't exist");
            }


        }else{
            var user = _mysqlClient.GetUserById(command.UserId);

            if (user != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. UserId: {command.UserId}");
                await _mysqlClient.DeleteUser(user);
                _logger.LogInformation($"User with id {command.UserId} has been deleted");

            }
            else
            {
                throw new System.Exception($"User with {command.UserId} doesn't exist");
            }
        }
    }

}
