
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

        public LocalController(
            LocalClient localClient,
            ILogger<LocalController> logger

         ){

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
            Console.WriteLine($"Local with made with localId: {localInfo.Id}");
            
            return await _localClient.CreateLocal(localInfo.Convert());

        }

         [HttpPatch]
        public async Task<GeneralResponse> UpdateLocal(LocalInfo localInfo)
        {
            Console.WriteLine($"Local with Id: {localInfo.Id} updating...");
            
            return await _localClient.UpdateLocal(localInfo.Convert());

        }


         [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteLocalById(int Id)
        {
            Console.WriteLine($"Local with Id: {Id} deleting...");
            
            return await _localClient.DeleteLocal( new Local{Id=Id});

        }
    }
}
