using System.Text.Json.Serialization;

namespace acmeat.api.local;
public class LocalInfo{

    [JsonConstructor]
    public LocalInfo(
                string Name,
                 string OpeningTime,
                 string ClosingTime,
                 string Address,
                 string OpeningDays,
                 bool Available,
                 string Url)
    {
        this.Id = new Random().Next();
        this.Name = Name;
        this.OpeningTime = OpeningTime;
        this.ClosingTime = ClosingTime;
        this.Address = Address;
        this.OpeningDays = OpeningDays;
        this.Available = Available;
        this.Url = Url;
    }


    public LocalInfo(acmeat.server.local.client.Local local){
        
        this.Id = local.Id;
        this.Name = local.Name;
        this.OpeningTime = local.OpeningTime;
        this.ClosingTime = local.ClosingTime;
        this.Address = local.Address;
        this.OpeningDays = local.OpeningDays;
        this.Available = local.Available;
        this.Url = local.Url;
        
    }

    public int Id { get; set; }

    public string Name { get; set; }

    public string OpeningTime { get; set; }

    public string ClosingTime { get; set; }

    public string Address { get; set; }

    public string OpeningDays { get; set; }
    public bool Available { get; set; }
     public string Url { get; set; }

        public acmeat.server.local.client.Local Convert()
    {
        acmeat.server.local.client.Local local = new acmeat.server.local.client.Local();
         local.Id = this.Id;
        local.Name = this.Name;
        local.OpeningDays = this.OpeningDays;
        local.OpeningTime = this.OpeningTime;
        local.ClosingTime = this.ClosingTime;
        local.Address = this.Address;
        local.Available = this.Available;
        local.Url = this.Url;
        return local;
    }
}