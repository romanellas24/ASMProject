using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.order;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class Order
{
    public Order()
    {
    }

    public Order(int UserId,
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

    public int Id { get; set; }

    [Column("ID_UTENTE")]
    public int UserId { get; set; }
    [Column("ID_LOCALE")]
    public int LocalId { get; set; }
    [Column("ID_SOC_C")]
    public int DeliveryCompanyId { get; set; }
    [Column("ORA_CONSEGNA")]
    public string DeliveryTime { get; set; }
    [Column("ORA_ACQUISTO")]
    public string PurchaseTime { get; set; }
    [Column("PREZZO")]
    public int Price { get; set; }
    [Column("ID_TRANSAZIONE")]
    public int TransactionId { get; set; }
    [Column("ID_MENU")]
    public int MenuId { get; set; }
    [Column("QUANTITÀ")]
    public int Quantity { get; set; }
}