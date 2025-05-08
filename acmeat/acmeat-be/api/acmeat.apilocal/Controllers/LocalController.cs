
using acmeat.server.local.client;
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.local
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class LocalController : ControllerBase
    {

        private readonly LocalClient _localClient;
        private readonly ILogger<LocalController> _logger;
        private readonly int deadLineHour = 10;

        public LocalController(
            LocalClient localClient,
            ILogger<LocalController> logger

         )
        {

            _logger = logger;
            _localClient = localClient;

        }


        [HttpGet("{Id}")]
        public async Task<LocalInfo> GetLocalById(int Id)
        {
            _logger.LogInformation($"Getting local with id: {Id}");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var local = await _localClient.GetLocalById(Id);
            return new LocalInfo(local);

        }

        [HttpGet]
        public async Task<List<LocalInfo>> GetLocals()
        {
            _logger.LogInformation($"Getting locals ");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var locals = await _localClient.GetLocalList();
            return locals.Locals.Select(x => new LocalInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateLocal(LocalInfo localInfo)
        {
            _logger.LogInformation($"Local with made with localId: {localInfo.Id}");

            return await _localClient.CreateLocal(localInfo.Convert());

        }

        [HttpPatch]
        public async Task<GeneralResponse> UpdateLocal(LocalInfo localInfo)
        {
            _logger.LogInformation($"Local with Id: {localInfo.Id} updating...");

            return await _localClient.UpdateLocal(localInfo.Convert());

        }


        [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteLocalById(int Id)
        {
            _logger.LogInformation($"Local with Id: {Id} deleting...");

            return await _localClient.DeleteLocal(new Local { Id = Id });

        }


        [HttpPost]
        public async Task<GeneralResponse> SetUnavailabilityByLocalId(int Id)
        {
            _logger.LogInformation($"Local with Id: {Id} is unavailable today");
            //https://learn.microsoft.com/en-us/dotnet/standard/datetime/how-to-use-dateonly-timeonly
            TimeOnly timeOnly = TimeOnly.FromDateTime(DateTime.Now);
            _logger.LogInformation($"Current time {timeOnly.Hour}:{timeOnly.Minute}");

            if (timeOnly.Hour > deadLineHour)
            {
                _logger.LogInformation($"Cannot update the local availability its too late...");
                GeneralResponse response = new GeneralResponse();
                response.Message = "Cannot update the local availability its too late...";
                return response;
            }
            else
            {
                _logger.LogInformation("Setting unavailability...");
                Local local = await _localClient.GetLocalById(Id);
                local.Available = false;
                return await _localClient.UpdateLocal(local);

            }
        }
    }
}
