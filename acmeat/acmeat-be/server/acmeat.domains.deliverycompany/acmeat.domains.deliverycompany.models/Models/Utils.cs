

using System.Collections.Generic;
using System.Linq;
namespace acmeat.server.deliverycompany;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public static class Utils{

    public static DeliveryCompany ConvertDbElementToServerElement(db.deliveryCompany.DeliveryCompany deliverycompany){
        return new DeliveryCompany(deliverycompany);
    }


    public static List<DeliveryCompany> ConvertDbListToServerList(List<db.deliveryCompany.DeliveryCompany> deliverycompanys){
          return deliverycompanys.Select(ConvertDbElementToServerElement).ToList();
    }

    public static db.deliveryCompany.DeliveryCompany ConvertServerElementIntoDbELement(DeliveryCompany deliverycompany){
        db.deliveryCompany.DeliveryCompany deliverycompanyDB = new db.deliveryCompany.DeliveryCompany(deliverycompany.Id, deliverycompany.Address, deliverycompany.Price, deliverycompany.Available,deliverycompany.Name);

        deliverycompanyDB.Id = deliverycompany.Id;

        return deliverycompanyDB;
    }
}