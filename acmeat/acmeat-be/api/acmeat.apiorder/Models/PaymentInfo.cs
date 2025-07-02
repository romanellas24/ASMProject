using acmeat.server.order.client;

namespace acmeat.api.order;

public class PaymentInfo{

    public PaymentInfo(
        // string IBAN,
        // string Causal,
        // int Price,
        string TransactionId
    ){
        // this.IBAN = IBAN;
        // this.Causal =Causal;
        // this.Price = Price;
        this.TransactionId = TransactionId;
    }




    // public string IBAN {get;set;}
    // public string Causal {get;set;}
    // public int Price {get;set;}
    public string TransactionId{get;set;}
}