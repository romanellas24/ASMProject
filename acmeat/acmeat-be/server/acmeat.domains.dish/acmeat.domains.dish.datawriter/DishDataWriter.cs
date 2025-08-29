using System.Threading.Tasks;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
namespace acmeat.server.dish.datawriter;



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DishDataWriter :
ICommandHandler<CreateNewDishCommand>,
ICommandHandler<UpdateNewDishCommand>,
ICommandHandler<DeleteNewDishCommand>
{
    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<DishDataWriter> _logger;

    public DishDataWriter(
        ILogger<DishDataWriter> logger,
        MysqlClient mysqlClient
    )
    {
        _logger = logger;
        _mysqlClient = mysqlClient;
    }

    public async Task SendAsync(CreateNewDishCommand command)
    {
        if (command.dish != null)
        {
            _logger.LogInformation($"Command: {command.GetType().Name}. DishId: {command.dish.Id}");
            await _mysqlClient.CreateDish(Utils.ConvertServerElementIntoDbELement(command.dish));
            _logger.LogInformation($"Dish with id {command.dish.Id} has been created");
        }
        else
        {
            throw new System.Exception($"Dish is null!");
        }

    }

    public async Task SendAsync(UpdateNewDishCommand command)
    {
        if (command.dish != null)
        {
            var dish = _mysqlClient.GetDishById(command.dish.Id);


            if (dish != null)
            {
                _logger.LogInformation($"Command: {command.GetType().Name}. DishId: {command.dish.Id}");
                await _mysqlClient.UpdateDish(command.dish.Convert());
                _logger.LogInformation($"Dish with id {command.dish.Id} has been updated");
            }else{
                throw new System.Exception($"Dish {command.dish.Id} has not been found");
            }

        }
        else
        {
            throw new System.Exception($"Dish is null!");
        }
    }


    public async Task SendAsync(DeleteNewDishCommand command)
    {

        if (command.dish != null)
        {

            var dish = _mysqlClient.GetDishById(command.dish.Id);

            if (dish != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. DishId: {command.dish.Id}");
                await _mysqlClient.DeleteDish(dish);
                _logger.LogInformation($"Dish with id {command.dish.Id} has been deleted");

            }
            else
            {
                throw new System.Exception($"Dish with {command.dish.Id} doesn't exist");
            }


        }else{
            var dish = _mysqlClient.GetDishById(command.DishId);

            if (dish != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. DishId: {command.DishId}");
                await _mysqlClient.DeleteDish(dish);
                _logger.LogInformation($"Dish with id {command.DishId} has been deleted");

            }
            else
            {
                throw new System.Exception($"Dish with {command.DishId} doesn't exist");
            }
        }
    }

}
