// Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse);
using System.Collections.Generic;

public class Address
    {
        public string addressLine { get; set; }
        public string streetName { get; set; }
        public string postalCode { get; set; }
        public string neighborhood { get; set; }
        public string locality { get; set; }
        public string formattedAddress { get; set; }
        public CountryRegion countryRegion { get; set; }
        public List<AdminDistrict> adminDistricts { get; set; }
    }

    public class AdminDistrict
    {
        public string shortName { get; set; }
    }

    public class CountryRegion
    {
        public string name { get; set; }
        public string ISO { get; set; }
    }

    public class Feature
    {
        public string type { get; set; }
        public Geometry geometry { get; set; }
        public List<double> bbox { get; set; }
        public Properties properties { get; set; }
    }

    public class GeocodePoint
    {
        public string calculationMethod { get; set; }
        public List<string> usageTypes { get; set; }
        public Geometry geometry { get; set; }
    }

    public class Geometry
    {
        public string type { get; set; }
        public List<double> coordinates { get; set; }
    }

    public class Properties
    {
        public string type { get; set; }
        public string confidence { get; set; }
        public List<string> matchCodes { get; set; }
        public List<GeocodePoint> geocodePoints { get; set; }
        public Address address { get; set; }
    }

    public class Location
    {
        public string type { get; set; }
        public List<Feature> features { get; set; }
    }

