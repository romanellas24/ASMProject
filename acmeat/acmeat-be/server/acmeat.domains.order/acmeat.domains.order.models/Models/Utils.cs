

using System.Collections.Generic;
using System.Linq;
namespace acmeat.server.order;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public static class Utils{

    public static Order ConvertDbElementToServerElement(db.order.Order order){
        return new Order(order);
    }


    public static List<Order> ConvertDbListToServerList(List<db.order.Order> orders){
          return orders.Select(ConvertDbElementToServerElement).ToList();
    }

    public static db.order.Order ConvertServerElementIntoDbELement(Order order){
        return new db.order.Order(order.UserId,
                                  order.LocalId,
                                  order.DeliveryCompanyId,
                                  order.DeliveryTime,
                                  order.PurchaseTime,
                                  order.Price,
                                  order.TransactionId,
                                  order.MenuId,
                                  order.Quantity);
    }
}