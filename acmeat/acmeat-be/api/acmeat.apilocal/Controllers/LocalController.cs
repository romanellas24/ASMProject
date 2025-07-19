
using acmeat.server.dish.client;
using acmeat.server.local.client;
using acmeat.server.menu.client;
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.local
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class LocalController : ControllerBase
    {

        private readonly LocalClient _localClient;
        private readonly MenuClient _menuClient;
        private readonly DishClient _dishClient;
        private readonly ILogger<LocalController> _logger;
        private readonly int deadLineHour = 10;

        public LocalController(
            LocalClient localClient,
            MenuClient menuClient,
            DishClient dishClient,
            ILogger<LocalController> logger

         )
        {

            _logger = logger;
            _localClient = localClient;
            _menuClient = menuClient;
            _dishClient = dishClient;

        }


        [HttpGet("{Id}")]
        public async Task<LocalInfo> GetLocalById(int Id)
        {
            _logger.LogInformation($"Getting local with id: {Id}");


            var local = await _localClient.GetLocalById(Id);
            return new LocalInfo(local);

        }

        [HttpGet]
        public async Task<List<LocalInfo>> GetLocals()
        {
            _logger.LogInformation($"Getting locals ");

            var locals = await _localClient.GetLocalList();
            return locals.Locals.Select(x => new LocalInfo(x)).ToList();

        }

        [HttpGet("{City}")]
        public async Task<List<LocalInfo>> GetLocalsByCity(string City)
        {
            _logger.LogInformation($"Getting locals from {City}");

            var locals = await _localClient.GetLocalListByCity(City);
            return locals.Locals.Select(x => new LocalInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<server.local.client.GeneralResponse> CreateLocal(LocalInfo localInfo)
        {
            _logger.LogInformation($"Local with made with localId: {localInfo.Id}");

            return await _localClient.CreateLocal(localInfo.Convert());

        }


        [HttpPost]
        public async Task<server.local.client.GeneralResponse> ApplyDailyUpdate(DailyUpdate dailyUpdate)
        {
            _logger.LogInformation($"Applying Daily Update from : {dailyUpdate.LocalUrl}");
            //https://learn.microsoft.com/en-us/dotnet/standard/datetime/how-to-use-dateonly-timeonly
            TimeOnly timeOnly = TimeOnly.FromDateTime(DateTime.Now);
            _logger.LogInformation($"Current time {timeOnly.Hour}:{timeOnly.Minute}");



            if (timeOnly.Hour < deadLineHour)
            {
                _logger.LogInformation($"Cannot update the local availability its too late...");
                server.local.client.GeneralResponse response = new server.local.client.GeneralResponse();
                response.Message = "Cannot update the local availability its too late...";
                return response;
            }
            else
            {


             _logger.LogInformation($"Getting Local with Url : {dailyUpdate.LocalUrl}");
            Local local = await _localClient.GetLocalByUrl(dailyUpdate.LocalUrl);
            Menu menu = new Menu()
            {
                Description = "Daily Menu-"+dailyUpdate.LocalUrl+"-"+DateTime.Now,
                LocalId = local.Id,
                Price = (int)dailyUpdate.dishes.Select(dish => dish.Price).Aggregate((price1,price2) => price1 + price2),
                Type = "Carne",
                Id = dailyUpdate.dishes.First().MenuId
                
            };

             _logger.LogInformation($"Creating new Menu with Id {menu.Id}");
            server.menu.client.GeneralResponse response = await _menuClient.CreateMenu(menu);

            if (response.Message != "OK")
            {
                throw new Exception("Menu updated failed for this reason: " + response.Message);
            }

             _logger.LogInformation($"Applying dishes to menu ");


            server.dish.client.GeneralResponse dishManagerResponse;

            dailyUpdate.dishes.ForEach(
                async dish =>
                {
                  
                    dishManagerResponse = await _dishClient.CreateDish(dish);


                    if (dishManagerResponse.Message != "OK")
                    {
                        throw new Exception("dish creation failed for this reason: " + dishManagerResponse.Message);
                    }
                }
            );

            return new server.local.client.GeneralResponse() { Message = "OK" };
               
            }




        }

        [HttpPost]
        public async Task<server.local.client.GeneralResponse> CheckLocalAvailability(LocalAvailabilty localAvailabilty)
        {
            _logger.LogInformation($"Checking Local availability: {localAvailabilty.localUrl} ");

            return await _localClient.CheckOrderAvailability(localAvailabilty.deliveryTime,localAvailabilty.dishes,localAvailabilty.localUrl);

        }


        [HttpPatch]
        public async Task<server.local.client.GeneralResponse> UpdateLocal(LocalInfo localInfo)
        {
            _logger.LogInformation($"Local with Id: {localInfo.Id} updating...");

            return await _localClient.UpdateLocal(localInfo.Convert());

        }


        [HttpDelete("{Id}")]
        public async Task<server.local.client.GeneralResponse> DeleteLocalById(int Id)
        {
            _logger.LogInformation($"Local with Id: {Id} deleting...");

            return await _localClient.DeleteLocal(new Local { Id = Id });

        }


        
    }
}
