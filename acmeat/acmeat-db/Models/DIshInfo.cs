

using acmeat.db.dish;

#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

public class DishInfo
{
    public int Id { get; set; }
    public int Quantity { get; set; }

    public DishInfo()
    {

    }


    public DishInfo(int Id, int Quantity)
    {

        this.Id = Id;
        this.Quantity = Quantity;

    }



}