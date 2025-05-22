using System.Text.Json.Serialization;

namespace acmeat.api.user;

public class UserCredentials
{

    [JsonConstructor]
    public UserCredentials(string Mail, string Pwd)
    {
        this.Mail = Mail;
        this.Pwd = Pwd;
    }


    public string Mail { get; set; }
    public string Pwd { get; set; }


}