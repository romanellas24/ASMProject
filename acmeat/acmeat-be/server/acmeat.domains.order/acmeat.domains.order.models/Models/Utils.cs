

using System.Collections.Generic;
using System.Linq;

public static class Utils{

    public static acmeat.server.order.Order ConvertDbElementToServerElement(acmeat.db.order.Order order){
        return new acmeat.server.order.Order(order);
    }


    public static List<acmeat.server.order.Order> ConvertDbListToServerList(List<acmeat.db.order.Order> orders){
          return orders.Select( x => new acmeat.server.order.Order(x) ).ToList();
    }
}