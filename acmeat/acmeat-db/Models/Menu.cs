using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.menu;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class Menu{
    public Menu()
    {
    }

    public Menu(int Id,string Descritpion, string Type, int Price)
    {
        this.Id = Id;
        this.Descritpion=Descritpion;
        this.Type = Type;
        this.Price = Price;
    }

    public int Id{get;set;}
    [Column("DESCRIZIONE")]
    public string Descritpion {get;set;}
     [Column("TIPO")]
    public string Type {get;set;}

     [Column("PREZZO")]
    public int Price {get;set;}
}