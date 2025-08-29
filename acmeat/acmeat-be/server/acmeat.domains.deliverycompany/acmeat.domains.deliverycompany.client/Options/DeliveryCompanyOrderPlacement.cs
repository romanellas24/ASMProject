
public class DeliveryCompanyOrderPlacement
{
    public int orderId { get; set; }
    public int vehicle { get; set; }
    public int timeMinutes { get; set; }
    public string expectedDeliveryTime { get; set; }
    public string companyName { get; set; }
    public string hash { get; set; }
    public string localAddress { get; set; }
    public string userAddress { get; set; }
}


    public class DeliveryCompanyOrderPlacementResponse
    {
        public Vehicle vehicle { get; set; }
        public int orderId { get; set; }
        public string startDeliveryTime { get; set; }
        public string endDeliveryTime { get; set; }
    }

    public class Vehicle
    {
        public int id { get; set; }
        public bool available { get; set; }
    }

