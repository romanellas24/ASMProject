using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.menu;
public class Menu{
    public Menu()
    {
    }

    public Menu(string Descritpion, string Type, int Price)
    {
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