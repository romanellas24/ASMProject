
using acmeat.server.deliverycompany.client;
using Microsoft.AspNetCore.Mvc;
using static acmeat.server.deliverycompany.client.DeliveryCompanyClient;

namespace acmeat.api.deliverycompany
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class DeliveryCompanyController : ControllerBase
    {

        private readonly DeliveryCompanyClient _deliverycompanyClient;
        private readonly ILogger<DeliveryCompanyController> _logger;

        public DeliveryCompanyController(
            DeliveryCompanyClient deliverycompanyClient,
            ILogger<DeliveryCompanyController> logger

         ){

            _logger = logger;
            _deliverycompanyClient = deliverycompanyClient;
            
        }


        [HttpGet("{Id}")]
        public async Task<DeliveryCompanyInfo> GetDeliveryCompanyById(int Id)
        {
            _logger.LogInformation($"Getting deliverycompany with id: {Id}");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var deliverycompany = await _deliverycompanyClient.GetDeliveryCompanyById(Id);
            return new DeliveryCompanyInfo(deliverycompany);

        }

        [HttpGet]
        public async Task<List<DeliveryCompanyInfo>> GetDeliveryCompanys()
        {
            _logger.LogInformation($"Getting deliverycompanys ");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var deliverycompanys = await _deliverycompanyClient.GetDeliveryCompanyList();
            return deliverycompanys.Deliverycompanys.Select(x => new DeliveryCompanyInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateDeliveryCompany(DeliveryCompanyInfo deliverycompanyInfo)
        {
            Console.WriteLine($"DeliveryCompany with made with deliverycompanyId: {deliverycompanyInfo.Id}");
            
            return await _deliverycompanyClient.CreateDeliveryCompany(deliverycompanyInfo.Convert());

        }

         [HttpPost]
        public async Task<GeneralResponse> CheckAvailabilityDeliveryCompany(AvailabilityPayload availabilityPayload)
        {
            Console.WriteLine($"DeliveryCompany with made with deliverycompanyId: {availabilityPayload.DeliveryCompanyUrl}");

            return await _deliverycompanyClient.CheckAvailability(availabilityPayload);

        }

         [HttpPatch]
        public async Task<GeneralResponse> UpdateDeliveryCompany(DeliveryCompanyInfo deliverycompanyInfo)
        {
            Console.WriteLine($"DeliveryCompany with Id: {deliverycompanyInfo.Id} updating...");
            
            return await _deliverycompanyClient.UpdateDeliveryCompany(deliverycompanyInfo.Convert());

        }


         [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteDeliveryCompanyById(int Id)
        {
            Console.WriteLine($"DeliveryCompany with Id: {Id} deleting...");
            
            return await _deliverycompanyClient.DeleteDeliveryCompany( new DeliveryCompany{Id=Id});

        }
    }
}
