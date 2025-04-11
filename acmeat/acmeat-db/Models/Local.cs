using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.local;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class Local{
    public Local()
    {
    }

    public Local(string Name,
                 string OpeningTime,
                 string ClosingTime,
                 string Address,
                 string OpeningDays,
                 bool Available)
    {
        this.Name=Name;
        this.OpeningTime = OpeningTime;
        this.ClosingTime = ClosingTime;
        this.Address = Address;
        this.OpeningDays = OpeningDays;
        this.Available = Available;
    }

    public int Id{get;set;}
    [Column("NOME")]
    public string Name {get;set;}
     [Column("ORA_APERTURA")]
    public string OpeningTime {get;set;}

     [Column("ORA_CHIUSURA")]
    public string ClosingTime {get;set;}
    [Column("INDIRIZZO")]
    public string Address {get;set;}

     [Column("GIORNI_APERTURA")]
    public string OpeningDays {get;set;}
     [Column("DISPONIBILE")]
    public bool Available {get;set;}
}