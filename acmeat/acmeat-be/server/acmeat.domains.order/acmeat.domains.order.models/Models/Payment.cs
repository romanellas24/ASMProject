namespace acmeat.server.order;

public class Payment{

    public Payment(
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



    public string IBAN {get;set;}
    public string Causal {get;set;}
    public int Price {get;set;}
    public int TransactionId{get;set;}
}