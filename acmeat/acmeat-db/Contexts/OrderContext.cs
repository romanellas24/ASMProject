

using Microsoft.EntityFrameworkCore;

namespace acmeat.db.order;

public class OrderContext:MySqlContext
{
    public OrderContext(IConfiguration configuration) : base(configuration)
    {
    }

    // private readonly MySqlContext context = context;

    public List<Order> GetOrders()
    {
        //  _logger.LogInformation("Getting Orders");
        List<Order> Orders = ORDINE
        .ToList();

        return Orders;
    }
    //fix update -> https://stackoverflow.com/questions/48202403/instance-of-entity-type-cannot-be-tracked-because-another-instance-with-same-key
    public Order GetOrderById(int id){
        Order Order = ORDINE.AsNoTracking()
        .Where( (Order) => Order.Id == id)
        .AsEnumerable()
        .First();

        return Order;
    }

    public async Task CreateOrder(Order Order){
        await ORDINE.AddAsync(Order);
        await SaveChangesAsync();
    }  


    public async Task UpdateOrder(Order Order){
       
        ORDINE.Update(Order);
        await SaveChangesAsync();
    }

    
    public async Task DeleteOrder(Order Order){
        ORDINE.Remove(Order);
        await SaveChangesAsync();
    }
    public async Task DeleteOrderById(int id)
    {
        Order OrderToDelete = new Order() { Id = id };
        Entry(OrderToDelete).State = EntityState.Deleted;
        await SaveChangesAsync();
    }
}