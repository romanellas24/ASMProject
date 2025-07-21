

using System.Text.Json.Serialization;

namespace acmeat.server.deliverycompany;
//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeliveryCompany
{

    [JsonConstructor]
    public DeliveryCompany(
               int Id, string Address, float Price, bool Available, string Name
               )
    {
        this.Id = Id;
        this.Address = Address;
        this.Price = Price;
        this.Available = Available;
        this.Name = Name;


    }

    public DeliveryCompany(db.deliveryCompany.DeliveryCompany deliverycompany)
    {
        this.Id = deliverycompany.Id;
        this.Address = deliverycompany.Address;
        this.Price = deliverycompany.Price;
        this.Available = deliverycompany.Available;
        this.Name = deliverycompany.Name;
    }
    public int Id { get; set; }
    public string Address { get; set; }
    
    public float Price { get; set; }
    public bool Available { get; set; }
    
    public string Name { get; set; }

    public acmeat.db.deliveryCompany.DeliveryCompany Convert()
    {
        acmeat.db.deliveryCompany.DeliveryCompany deliverycompany = new acmeat.db.deliveryCompany.DeliveryCompany();
        deliverycompany.Id = this.Id;
        deliverycompany.Address = this.Address;
        deliverycompany.Available = this.Available;
        deliverycompany.Price = (int)this.Price;
        deliverycompany.Name = this.Name;
        return deliverycompany;
    }
}