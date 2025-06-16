using acmeat.server.order.client;

namespace acmeat.api.order;

public class PaymentInfo{

    public PaymentInfo(
        string IBAN,
        string Causal,
        int Price,
        int TransactionId
    ){
        this.IBAN = IBAN;
        this.Causal =Causal;
        this.Price = Price;
        this.TransactionId = TransactionId;
    }

    public PaymentInfo(
        Payment payment
    ){
        this.IBAN = payment.IBAN;
        this.Causal =payment.Causal;
        this.Price = payment.Price;
        this.TransactionId = payment.TransactionId;
    }



    public string IBAN {get;set;}
    public string Causal {get;set;}
    public int Price {get;set;}
    public int TransactionId{get;set;}
}