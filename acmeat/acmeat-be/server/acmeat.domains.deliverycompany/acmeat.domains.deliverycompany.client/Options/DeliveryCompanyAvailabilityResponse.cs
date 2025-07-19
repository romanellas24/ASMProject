// Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse);
    public class DeliveryCompanyAvailabilityResponse
    {
        public bool isVehicleAvailable { get; set; }
        public double price { get; set; }
        public int time { get; set; }
        public double distance { get; set; }
        public int vehicleId { get; set; }
    }


public class DeliveryCompanyAvailabilityResponse2v
{
    public bool isVehicleAvailable { get; set; }
    public double price { get; set; }
    public int time { get; set; }
    public double distance { get; set; }
    public int vehicleId { get; set; }
    public string DeliveryCompanyUrl { get; set; } 
    }

