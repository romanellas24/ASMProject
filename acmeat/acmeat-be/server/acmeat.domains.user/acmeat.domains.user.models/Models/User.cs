


using System;
using System.Text.Json.Serialization;

namespace acmeat.server.user;
//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class User
{

    [JsonConstructor]
    public User(
                int Id,
                string Address,
                string Mail,
                string Pwd
               )
    {
        this.Id = Id;
        this.Address = Address;
        this.Mail = Mail;
        this.Pwd = Pwd;
        

    }

    public User(db.user.User user)
    {
        this.Id = user.Id;
        this.Address = user.Address;
        this.Mail = user.Mail;
        this.Pwd = user.Pwd;
        

    }
    public int Id { get; set; }

    public string Address { get; set; }


    public string Mail { get; set; }


    public string Pwd { get; set; }

    public acmeat.db.user.User Convert()
    {
        acmeat.db.user.User user = new acmeat.db.user.User();
        user.Id = this.Id;
        user.Address = this.Address;
        user.Mail = this.Mail;
        user.Pwd = this.Pwd;
        return user;
    }
}