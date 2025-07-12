using acmeat.server.order.client;

namespace acmeat.api.order;

public class FinishOrder
{

    public FinishOrder(
        int OrderId,
        string Reason
    )
    {
        this.OrderId = OrderId;
        this.Reason = Reason;

    }




    // public string IBAN {get;set;}
    // public string Causal {get;set;}
    // public int Price {get;set;}
    public int OrderId { get; set; }
    public string Reason { get; set; }
}