using System.Text.Json.Serialization;

namespace acmeat.api.order;
public class OrderInfo{



    [JsonConstructor]
    public OrderInfo(
                    int UserId,
                     string DeliveryTime,
                     int DeliveryCompanyId,
                     string PurchaseTime,
                     string TransactionId,
                     int Price,
                     int MenuId,
                     int Quantity,
                     int LocalId
                     )
    {
        this.Id = new Random().Next();
        this.UserId = UserId;
        this.DeliveryTime = DeliveryTime;
        this.PurchaseTime = PurchaseTime;
        this.TransactionId = TransactionId;
        this.DeliveryCompanyId = DeliveryCompanyId;
        this.Price = Price;
        this.MenuId = MenuId;
        this.Quantity = Quantity;
        this.LocalId = LocalId;
        
    }

    public OrderInfo(acmeat.server.order.client.Order order){
        
        this.Id = order.Id;
        this.UserId = order.UserId;
        this.DeliveryTime = order.DeliveryTime;
        this.DeliveryCompanyId = order.DeliveryCompanyId;
        this.PurchaseTime = order.PurchaseTime;
        this.TransactionId = order.TransactionId;
        this.Price = order.Price;
        this.MenuId = order.MenuId;
        this.Quantity = order.Quantity;
        this.LocalId = order.LocalId;
        
    }

    public int UserId{get;set;}
    public int Id {get;set;}
    public string DeliveryTime {get;set;}
    public int DeliveryCompanyId {get;set;}
    public string PurchaseTime { get; set; }
    public string TransactionId {get;set;}
    public int Price {get;set;}
    public int MenuId {get;set;}
    public int Quantity {get;set;}
    public int LocalId{get;set;}


    public acmeat.server.order.client.Order Convert(){
        acmeat.server.order.client.Order order = new acmeat.server.order.client.Order();
        order.Id = this.Id;
        order.UserId = this.UserId;
        order.DeliveryTime = this.DeliveryTime;
        order.PurchaseTime = this.PurchaseTime;
        order.TransactionId = this.TransactionId;
        order.DeliveryCompanyId = this.DeliveryCompanyId;
        order.Price = this.Price;
        order.MenuId = this.MenuId;
        order.Quantity = this.Quantity;
        order.LocalId = this.LocalId;
        return order;
    }

    
}