using System.Threading.Tasks;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
namespace acmeat.server.order.datawriter;



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class OrderDataWriter :
ICommandHandler<CreateNewOrderCommand>,
ICommandHandler<UpdateNewOrderCommand>,
ICommandHandler<DeleteNewOrderCommand>
{
    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<OrderDataWriter> _logger;

    public OrderDataWriter(
        ILogger<OrderDataWriter> logger,
        MysqlClient mysqlClient
    )
    {
        _logger = logger;
        _mysqlClient = mysqlClient;
    }

    public async Task SendAsync(CreateNewOrderCommand command)
    {
        if (command.order != null)
        {
            _logger.LogInformation($"Command: {command.GetType().Name}. OrderId: {command.order.Id}");
            await _mysqlClient.CreateOrder(Utils.ConvertServerElementIntoDbELement(command.order));
            _logger.LogInformation($"Order with id {command.order.Id} has been created");
        }
        else
        {
            throw new System.Exception($"Order is null!");
        }

    }

    public async Task SendAsync(UpdateNewOrderCommand command)
    {
        if (command.order != null)
        {
            var order = _mysqlClient.GetOrderById(command.order.Id);


            if (order != null)
            {
                _logger.LogInformation($"Command: {command.GetType().Name}. OrderId: {command.order.Id}");
                await _mysqlClient.UpdateOrder(Utils.ConvertServerElementIntoDbELement(command.order));
                _logger.LogInformation($"Order with id {command.order.Id} has been updated");
            }else{
                throw new System.Exception($"Order {command.order.Id} has not been found");
            }

        }
        else
        {
            throw new System.Exception($"Order is null!");
        }
    }


    public async Task SendAsync(DeleteNewOrderCommand command)
    {

        if (command.order != null)
        {

            var order = _mysqlClient.GetOrderById(command.order.Id);

            if (order != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. OrderId: {command.order.Id}");
                await _mysqlClient.DeleteOrder(Utils.ConvertServerElementIntoDbELement(command.order));
                _logger.LogInformation($"Order with id {command.order.Id} has been created");

            }
            else
            {
                throw new System.Exception($"Order with {command.order.Id} doesn't exist");
            }


        }
    }

}
