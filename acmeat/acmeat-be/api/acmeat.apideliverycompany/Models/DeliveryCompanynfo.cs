using System.Text.Json.Serialization;

namespace acmeat.api.deliverycompany;
public class DeliveryCompanyInfo
{

    [JsonConstructor]
    public DeliveryCompanyInfo(
                string Address, int Price, bool Available)
    {
        this.Id = new Random().Next();
     this.Address = Address;
        this.Price = Price;
        this.Available = Available;

    }


    public DeliveryCompanyInfo(acmeat.server.deliverycompany.client.DeliveryCompany deliverycompany)
    {

       this.Id = deliverycompany.Id;
        this.Address = deliverycompany.Address;
        this.Price = deliverycompany.Price;
        this.Available = deliverycompany.Available;

    }

    public int Id { get; set; }
    public string Address { get; set; }
    
    public int Price { get; set; }
    public bool Available { get; set; }
    public acmeat.server.deliverycompany.client.DeliveryCompany Convert()
    {
        acmeat.server.deliverycompany.client.DeliveryCompany deliverycompany = new acmeat.server.deliverycompany.client.DeliveryCompany();
         deliverycompany.Id = this.Id;
        deliverycompany.Address =  this.Address;
        deliverycompany.Available = this.Available;
        deliverycompany.Price = this.Price ;
        return deliverycompany;
    }
}