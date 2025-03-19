namespace acmeat.api.order;
public class OrderInfo{

    public OrderInfo(int UserId,
                     string DeliveryTime,
                     string PurchaseTime,
                     int TransactionId,
                     int Price,
                     int MenuId,
                     int Quantity
                     )
    {
        this.UserId = UserId;
        this.DeliveryTime = DeliveryTime;
        this.PurchaseTime = PurchaseTime;
        this.TransactionId = TransactionId;
        this.Price = Price;
        this.MenuId = MenuId;
        this.Quantity = Quantity;
        
    }

    public int UserId{get;set;}
    public int Id {get;set;}
    public string DeliveryTime {get;set;}
    public string PurchaseTime {get;set;}
    public int TransactionId {get;set;}
    public int Price {get;set;}
    public int MenuId {get;set;}
    public int Quantity {get;set;}
}