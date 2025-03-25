
using Microsoft.EntityFrameworkCore;

namespace acmeat.db.deliveryCompany;
public class DeliveryCompanyContext(MySqlContext context)
{

    private readonly MySqlContext _context = context;

        public List<DeliveryCompany> GetDeliveryCompanies()
    {
        //  _logger.LogInformation("Getting DeliveryCompanys");
        List<DeliveryCompany> DeliveryCompanys = _context.SOCIETÀ_CONSEGNA
        .ToList();

        return DeliveryCompanys;
    }

    public DeliveryCompany GetDeliveryCompanyById(int id){
        DeliveryCompany DeliveryCompany = _context.SOCIETÀ_CONSEGNA
        .Where( (DeliveryCompany) => DeliveryCompany.Id == id)
        .AsEnumerable()
        .First();

        return DeliveryCompany;
    }

    public async Task CreateDeliveryCompany(DeliveryCompany DeliveryCompany){
        await _context.SOCIETÀ_CONSEGNA.AddAsync(DeliveryCompany);
        await _context.SaveChangesAsync();
    }  


    public async Task UpdateDeliveryCompany(DeliveryCompany DeliveryCompany){
        _context.SOCIETÀ_CONSEGNA.Update(DeliveryCompany);
        await _context.SaveChangesAsync();
    }

    
    public async Task DeleteDeliveryCompany(DeliveryCompany DeliveryCompany){
        _context.SOCIETÀ_CONSEGNA.Remove(DeliveryCompany);
        await _context.SaveChangesAsync();
    }
    public async Task DeleteDeliveryCompanyById(int id)
    {
        DeliveryCompany DeliveryCompanyToDelete = new DeliveryCompany() { Id = id };
        _context.Entry(DeliveryCompanyToDelete).State = EntityState.Deleted;
        await _context.SaveChangesAsync();
    }
}