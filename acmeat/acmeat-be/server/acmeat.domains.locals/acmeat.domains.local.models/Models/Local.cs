

using System.Text.Json.Serialization;

namespace acmeat.server.local;
//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class Local
{

    [JsonConstructor]
    public Local(
               int Id,
                 string Name,
                 string OpeningTime,
                 string ClosingTime,
                 string Address,
                 string OpeningDays,
                 bool Available
               )
    {
        this.Id = Id;
        this.Name=Name;
        this.OpeningTime = OpeningTime;
        this.ClosingTime = ClosingTime;
        this.Address = Address;
        this.OpeningDays = OpeningDays;
        this.Available = Available;


    }

    public Local(db.local.Local local)
    {
        this.Id = local.Id;
        this.Name=local.Name;
        this.OpeningTime = local.OpeningTime;
        this.ClosingTime = local.ClosingTime;
        this.Address = local.Address;
        this.OpeningDays = local.OpeningDays;
        this.Available = local.Available;


    }
    public int Id { get; set; }

    public string Name { get; set; }

    public string OpeningTime { get; set; }

    public string ClosingTime { get; set; }

    public string Address { get; set; }

    public string OpeningDays { get; set; }
    public bool Available { get; set; }

    public acmeat.db.local.Local Convert()
    {
        acmeat.db.local.Local local = new acmeat.db.local.Local();
        local.Id = this.Id;
        local.Name = this.Name;
        local.OpeningDays = this.OpeningDays;
        local.OpeningTime = this.OpeningTime;
        local.ClosingTime = this.ClosingTime;
        local.Address = this.Address;
        local.Available = this.Available;
        
        return local;
    }
}