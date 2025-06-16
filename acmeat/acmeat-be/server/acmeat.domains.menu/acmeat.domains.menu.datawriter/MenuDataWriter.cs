using System.Threading.Tasks;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
namespace acmeat.server.menu.datawriter;



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class MenuDataWriter :
ICommandHandler<CreateNewMenuCommand>,
ICommandHandler<UpdateNewMenuCommand>,
ICommandHandler<DeleteNewMenuCommand>
{
    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<MenuDataWriter> _logger;

    public MenuDataWriter(
        ILogger<MenuDataWriter> logger,
        MysqlClient mysqlClient
    )
    {
        _logger = logger;
        _mysqlClient = mysqlClient;
    }

    public async Task SendAsync(CreateNewMenuCommand command)
    {
        if (command.menu != null)
        {
            _logger.LogInformation($"Command: {command.GetType().Name}. MenuId: {command.menu.Id}");
            await _mysqlClient.CreateMenu(Utils.ConvertServerElementIntoDbELement(command.menu));
            _logger.LogInformation($"Menu with id {command.menu.Id} has been created");
        }
        else
        {
            throw new System.Exception($"Menu is null!");
        }

    }

    public async Task SendAsync(UpdateNewMenuCommand command)
    {
        if (command.menu != null)
        {
            var menu = _mysqlClient.GetMenuById(command.menu.Id);


            if (menu != null)
            {
                _logger.LogInformation($"Command: {command.GetType().Name}. MenuId: {command.menu.Id}");
                await _mysqlClient.UpdateMenu(command.menu.Convert());
                _logger.LogInformation($"Menu with id {command.menu.Id} has been updated");
            }else{
                throw new System.Exception($"Menu {command.menu.Id} has not been found");
            }

        }
        else
        {
            throw new System.Exception($"Menu is null!");
        }
    }


    public async Task SendAsync(DeleteNewMenuCommand command)
    {

        if (command.menu != null)
        {

            var menu = _mysqlClient.GetMenuById(command.menu.Id);

            if (menu != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. MenuId: {command.menu.Id}");
                await _mysqlClient.DeleteMenu(menu);
                _logger.LogInformation($"Menu with id {command.menu.Id} has been deleted");

            }
            else
            {
                throw new System.Exception($"Menu with {command.menu.Id} doesn't exist");
            }


        }else{
            var menu = _mysqlClient.GetMenuById(command.MenuId);

            if (menu != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. MenuId: {command.MenuId}");
                await _mysqlClient.DeleteMenu(menu);
                _logger.LogInformation($"Menu with id {command.MenuId} has been deleted");

            }
            else
            {
                throw new System.Exception($"Menu with {command.MenuId} doesn't exist");
            }
        }
    }

}
