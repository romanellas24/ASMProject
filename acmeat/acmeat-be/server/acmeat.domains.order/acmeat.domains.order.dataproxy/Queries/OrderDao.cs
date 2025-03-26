using System.Collections.Generic;
using acmeat.server.order;

namespace acmeat.server.order.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public interface OrderDao
  {
    public Order GetOrderById(int productId);
    public List<Order> GetOrders();
  }