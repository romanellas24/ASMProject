using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.deliveryCompany;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class DeliveryCompany
{
    public DeliveryCompany()
    {
    }

    public DeliveryCompany(int Id, string Address, float Price, bool Available, string Name)
    {
        this.Id = Id;
        this.Address = Address;
        this.Price = Price;
        this.Available = Available;
        this.Name = Name;
    }

    public int Id { get; set; }
    [Column("INDIRIZZO")]
    public string Address { get; set; }
    [Column("PREZZO")]
    public float Price { get; set; }
    [Column("DISPONIBILE")]
    public bool Available { get; set; }

    [Column("NOME")]
    public string Name{ get; set; }
}