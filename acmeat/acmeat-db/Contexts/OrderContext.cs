

using Microsoft.EntityFrameworkCore;

namespace acmeat.db.order;

public class OrderContext(MySqlContext context)
{

    private readonly MySqlContext _context = context;

      public List<Order> GetOrders()
    {
        //  _logger.LogInformation("Getting Orders");
        List<Order> Orders = _context.ORDINE
        .ToList();

        return Orders;
    }

    public Order GetOrderById(int id){
        Order Order = _context.ORDINE
        .Where( (Order) => Order.Id == id)
        .AsEnumerable()
        .First();

        return Order;
    }

    public async Task CreateOrder(Order Order){
        await _context.ORDINE.AddAsync(Order);
        await _context.SaveChangesAsync();
    }  


    public async Task UpdateOrder(Order Order){
        _context.ORDINE.Update(Order);
        await _context.SaveChangesAsync();
    }

    
    public async Task DeleteOrder(Order Order){
        _context.ORDINE.Remove(Order);
        await _context.SaveChangesAsync();
    }
    public async Task DeleteOrderById(int id)
    {
        Order OrderToDelete = new Order() { Id = id };
        _context.Entry(OrderToDelete).State = EntityState.Deleted;
        await _context.SaveChangesAsync();
    }
}