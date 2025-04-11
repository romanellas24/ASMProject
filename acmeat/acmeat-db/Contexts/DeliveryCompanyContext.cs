
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.deliveryCompany;
public class DeliveryCompanyContext:MySqlContext
{


    public DeliveryCompanyContext(IConfiguration configuration) : base(configuration)
    {
    }

    public List<DeliveryCompany> GetDeliveryCompanies()
    {
        //  _logger.LogInformation("Getting DeliveryCompanys");
        List<DeliveryCompany> DeliveryCompanys = SOCIETÀ_CONSEGNA
        .ToList();

        return DeliveryCompanys;
    }
     //fix update -> https://stackoverflow.com/questions/48202403/instance-of-entity-type-cannot-be-tracked-because-another-instance-with-same-key
    public DeliveryCompany GetDeliveryCompanyById(int id){
        DeliveryCompany DeliveryCompany = SOCIETÀ_CONSEGNA.AsNoTracking()
        .Where( (DeliveryCompany) => DeliveryCompany.Id == id)
        .AsEnumerable()
        .First();

        return DeliveryCompany;
    }

    public async Task CreateDeliveryCompany(DeliveryCompany DeliveryCompany){
        await SOCIETÀ_CONSEGNA.AddAsync(DeliveryCompany);
        await SaveChangesAsync();
    }  


    public async Task UpdateDeliveryCompany(DeliveryCompany DeliveryCompany){
        SOCIETÀ_CONSEGNA.Update(DeliveryCompany);
        await SaveChangesAsync();
    }

    
    public async Task DeleteDeliveryCompany(DeliveryCompany DeliveryCompany){
        SOCIETÀ_CONSEGNA.Remove(DeliveryCompany);
        await SaveChangesAsync();
    }
    public async Task DeleteDeliveryCompanyById(int id)
    {
        DeliveryCompany DeliveryCompanyToDelete = new DeliveryCompany() { Id = id };
        Entry(DeliveryCompanyToDelete).State = EntityState.Deleted;
        await SaveChangesAsync();
    }
}