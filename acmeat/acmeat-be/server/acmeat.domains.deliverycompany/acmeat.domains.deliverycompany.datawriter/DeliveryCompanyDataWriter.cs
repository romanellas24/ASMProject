using System.Threading.Tasks;
using acmeat.db.mysql;
using Microsoft.Extensions.Logging;
namespace acmeat.server.deliverycompany.datawriter;



//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeliveryCompanyDataWriter :
ICommandHandler<CreateNewDeliveryCompanyCommand>,
ICommandHandler<UpdateNewDeliveryCompanyCommand>,
ICommandHandler<DeleteNewDeliveryCompanyCommand>
{
    private readonly MysqlClient _mysqlClient;
    private readonly ILogger<DeliveryCompanyDataWriter> _logger;

    public DeliveryCompanyDataWriter(
        ILogger<DeliveryCompanyDataWriter> logger,
        MysqlClient mysqlClient
    )
    {
        _logger = logger;
        _mysqlClient = mysqlClient;
    }

    public async Task SendAsync(CreateNewDeliveryCompanyCommand command)
    {
        if (command.deliverycompany != null)
        {
            _logger.LogInformation($"Command: {command.GetType().Name}. DeliveryCompanyId: {command.deliverycompany.Id}");
            await _mysqlClient.CreateDeliveryCompany(Utils.ConvertServerElementIntoDbELement(command.deliverycompany));
            _logger.LogInformation($"DeliveryCompany with id {command.deliverycompany.Id} has been created");
        }
        else
        {
            throw new System.Exception($"DeliveryCompany is null!");
        }

    }

    public async Task SendAsync(UpdateNewDeliveryCompanyCommand command)
    {
        if (command.deliverycompany != null)
        {
            var deliverycompany = _mysqlClient.GetDeliveryCompanyById(command.deliverycompany.Id);


            if (deliverycompany != null)
            {
                _logger.LogInformation($"Command: {command.GetType().Name}. DeliveryCompanyId: {command.deliverycompany.Id}");
                await _mysqlClient.UpdateDeliveryCompany(command.deliverycompany.Convert());
                _logger.LogInformation($"DeliveryCompany with id {command.deliverycompany.Id} has been updated");
            }else{
                throw new System.Exception($"DeliveryCompany {command.deliverycompany.Id} has not been found");
            }

        }
        else
        {
            throw new System.Exception($"DeliveryCompany is null!");
        }
    }


    public async Task SendAsync(DeleteNewDeliveryCompanyCommand command)
    {

        if (command.deliverycompany != null)
        {

            var deliverycompany = _mysqlClient.GetDeliveryCompanyById(command.deliverycompany.Id);

            if (deliverycompany != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. DeliveryCompanyId: {command.deliverycompany.Id}");
                await _mysqlClient.DeleteDeliveryCompany(deliverycompany);
                _logger.LogInformation($"DeliveryCompany with id {command.deliverycompany.Id} has been deleted");

            }
            else
            {
                throw new System.Exception($"DeliveryCompany with {command.deliverycompany.Id} doesn't exist");
            }


        }else{
            var deliverycompany = _mysqlClient.GetDeliveryCompanyById(command.DeliveryCompanyId);

            if (deliverycompany != null)
            {

                _logger.LogInformation($"Command: {command.GetType().Name}. DeliveryCompanyId: {command.DeliveryCompanyId}");
                await _mysqlClient.DeleteDeliveryCompany(deliverycompany);
                _logger.LogInformation($"DeliveryCompany with id {command.DeliveryCompanyId} has been deleted");

            }
            else
            {
                throw new System.Exception($"DeliveryCompany with {command.DeliveryCompanyId} doesn't exist");
            }
        }
    }

}
