using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.deliveryCompany;
public class DeliveryCompany
{
    public DeliveryCompany()
    {
    }

    public DeliveryCompany(string Address, int Price, bool Available)
    {
        this.Address = Address;
        this.Price = Price;
        this.Available = Available;
    }

    public int Id { get; set; }
    [Column("INDIRIZZO")]
    public string Address { get; set; }
    [Column("PREZZO")]
    public int Price { get; set; }
    [Column("DISPONIBILE")]
    public bool Available { get; set; }
}