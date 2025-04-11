
//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

using System;
using System.Text.Json.Serialization;

namespace acmeat.server.order;
public class Order
{

     [JsonConstructor]
    public Order(
                 int Id,
                 int UserId,
                 int LocalId,
                 int DeliveryCompanyId,
                 string DeliveryTime,
                 string PurchaseTime,
                 int Price,
                 int TransactionId,
                 int MenuId,
                 int Quantity
                )
    {
        this.Id = Id;
        this.UserId = UserId;
        this.LocalId = LocalId;
        this.DeliveryCompanyId = DeliveryCompanyId;
        this.DeliveryTime = DeliveryTime;
        this.PurchaseTime = PurchaseTime;
        this.Price = Price;
        this.TransactionId = TransactionId;
        this.MenuId = MenuId;
        this.Quantity = Quantity;
        
    }

        public Order(acmeat.db.order.Order order)
    {
        this.Id = order.Id;
        this.UserId = order.UserId;
        this.LocalId = order.LocalId;
        this.DeliveryCompanyId = order.DeliveryCompanyId;
        this.DeliveryTime = order.DeliveryTime;
        this.PurchaseTime = order.PurchaseTime;
        this.Price = order.Price;
        this.TransactionId = order.TransactionId;
        this.MenuId = order.MenuId;
        this.Quantity = order.Quantity;
        
    }
    public int Id { get; set; }

    public int UserId { get; set; }
      public int LocalId { get; set; }
    
    public int DeliveryCompanyId { get; set; }
  
    public string DeliveryTime { get; set; }

    public string PurchaseTime { get; set; }

    public int Price { get; set; }

    public int TransactionId { get; set; }

    public int MenuId { get; set; }

    public int Quantity { get; set; }

    public acmeat.db.order.Order Convert(){
        acmeat.db.order.Order order = new acmeat.db.order.Order();
        order.Id = this.Id;
        order.UserId = this.UserId;
        order.DeliveryTime = this.DeliveryTime;
        order.PurchaseTime = this.PurchaseTime;
        order.TransactionId = this.TransactionId;
        order.Price = this.Price;
        order.MenuId = this.MenuId;
        order.Quantity = this.Quantity;
        return order;
    }
}