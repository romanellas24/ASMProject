using System.Text.Json.Serialization;

namespace acmeat.api.user;

public class UserInfo
{

    [JsonConstructor]
    public UserInfo(string Address, string Mail, string Pwd)
    {
        this.Id = new Random().Next();
        this.Address = Address;
        this.Mail = Mail;
        this.Pwd = Pwd;
    }


    public UserInfo(acmeat.server.user.client.User user)
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

    public acmeat.server.user.client.User Convert()
    {
        acmeat.server.user.client.User user = new acmeat.server.user.client.User();
        user.Id = this.Id;
        user.Address = this.Address;
        user.Mail = this.Mail;
        user.Pwd = this.Pwd;
        return user;
    }
    
}