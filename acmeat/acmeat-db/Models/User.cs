using System.ComponentModel.DataAnnotations.Schema;

namespace acmeat.db.user;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class User{
    public User()
    {
    }

    public User(string Address, string Mail, string Pwd)
    {
        this.Address=Address;
        this.Mail = Mail;
        this.Pwd = Pwd;
    }

    public int Id{get;set;}
    [Column("INDIRIZZO")]
    public string Address {get;set;}
     [Column("EMAIL")]
    public string Mail {get;set;}

     [Column("PWD")]
    public string Pwd {get;set;}
}