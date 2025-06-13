using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.dish;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class Dish
{
    public Dish()
    {
    }

    public Dish(int Id, string Name, string Description, int Price, int MenuId, string Date)
    {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.MenuId = MenuId;
        this.Date = Date;
    }

    public int Id { get; set; }
    [Column("DESCRIZIONE")]
    public string Description { get; set; }
    [Column("NOME")]
    public string Name { get; set; }


    [Column("PREZZO")]
    public int Price { get; set; }

    [Column("MENU_ID")]
    public int MenuId { get; set; }
    
    [Column("DATA")]
    public string Date {get;set;}
}