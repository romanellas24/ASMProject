using System.Text.Json.Serialization;
using acmeat.server.order.client;

namespace acmeat.api.order;

public class PaymentInfo
{
    [JsonConstructor]
    public PaymentInfo(
       // string IBAN,
       // string Causal,
       // int Price,
       int OrderId,
       string TransactionId
   )
    {
        // this.IBAN = IBAN;
        // this.Causal =Causal;
        // this.Price = Price;
        this.OrderId = OrderId;
        this.TransactionId = TransactionId;
    }




    // public string IBAN {get;set;}
    // public string Causal {get;set;}
    // public int Price {get;set;}
    public int OrderId { get; set; }
    public string TransactionId { get; set; }
    
    public acmeat.server.order.client.Payment Convert(){
        acmeat.server.order.client.Payment payment = new acmeat.server.order.client.Payment();
        payment.OrderId = this.OrderId;
        payment.TransactionId = this.TransactionId;
        return payment;
    }

}