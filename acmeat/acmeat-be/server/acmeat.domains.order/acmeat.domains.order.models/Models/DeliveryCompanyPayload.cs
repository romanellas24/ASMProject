public class DeliveryCompanyPayload
{
    public string LocalAddress { get; set; }
    public string UserAddress { get; set; }
    public string DeliveryTime { get; set; }
    public string DeliveryCompanyUrl { get; set; }
    public int OrderId { get; set; }
    public int Vehicle { get; set; } // Assuming it's a string, adjust type if needed
    public int TimeMinutes { get; set; }
    public bool IsVehicleAvailable { get; set; }
    public float Price { get; set; }
    public double Distance { get; set; }
}