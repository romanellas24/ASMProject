using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.dish;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class Dish
{
    public Dish()
    {
    }

    public Dish(int Id, string Name, string Description, float Price, int MenuId)
    {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.MenuId = MenuId;
    }

    public int Id { get; set; }
    [Column("DESCRIZIONE")]
    public string Description { get; set; }
    [Column("NOME")]
    public string Name { get; set; }


    [Column("PREZZO")]
    public float Price { get; set; }

    [Column("MENU_ID")]
    public int MenuId { get; set; }
}