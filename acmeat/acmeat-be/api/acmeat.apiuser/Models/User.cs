namespace acmeat.api.user;
public class UserInfo{

    public UserInfo(string Address, string Mail, string Pwd)
    {
        this.Address=Address;
        this.Mail = Mail;
        this.Pwd = Pwd;
    }

    public int Id{get;set;}
    public string Address {get;set;}
    public string Mail {get;set;}
    public string Pwd {get;set;}
}