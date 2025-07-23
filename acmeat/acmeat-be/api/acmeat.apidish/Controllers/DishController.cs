
using acmeat.server.dish.client;
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.dish
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class DishController : ControllerBase
    {

        private readonly DishClient _dishClient;
        private readonly ILogger<DishController> _logger;

        public DishController(
            DishClient dishClient,
            ILogger<DishController> logger

         ){

            _logger = logger;
            _dishClient = dishClient;
            
        }


        [HttpGet("{Id}")]
        public async Task<DishInfo> GetDishById(int Id)
        {
            _logger.LogInformation($"Getting dish with id: {Id}");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var dish = await _dishClient.GetDishById(Id);
            return new DishInfo(dish);

        }

         [HttpGet("{MenuId}")]
        public async Task<List<DishInfo>> GetDishsByMenuId(int MenuId)
        {
            _logger.LogInformation($"Getting dishs from Local with Id: {MenuId}");


            var dishs = await _dishClient.GetDishsByMenuId(MenuId);
            return dishs.Dishs.Select(x => new DishInfo(x)).ToList();

        }

        [HttpGet]
        public async Task<List<DishInfo>> GetDishs()
        {
            _logger.LogInformation($"Getting dishs ");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var dishs = await _dishClient.GetDishList();
            return dishs.Dishs.Select(x => new DishInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateDish(DishInfo dishInfo)
        {
            _logger.LogInformation($"Dish with made with dishId: {dishInfo.Id}");
            
            return await _dishClient.CreateDish(dishInfo.Convert());

        }

         [HttpPatch]
        public async Task<GeneralResponse> UpdateDish(DishInfo dishInfo)
        {
            _logger.LogInformation($"Dish with Id: {dishInfo.Id} updating...");
            
            return await _dishClient.UpdateDish(dishInfo.Convert());

        }

        [HttpPatch]
        public async Task<GeneralResponse> UpdateDishs(List<DishInfo> dishList)
        {
            _logger.LogInformation($"Updating Dishs from Local Id: {dishList.First().MenuId} updating {dishList.Count} elements.");
            GeneralResponse response = new GeneralResponse();
            DishList dishListClient = new DishList();


                foreach(DishInfo dish in dishList){
                    dishListClient.Dishs.Add(dish.Convert());
                }

                
                
                return await _dishClient.UpdateDishs(dishListClient);;

            
            // return await _dishClient.UpdateDish(dishInfo.Convert());

        }


         [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteDishById(int Id)
        {
            _logger.LogInformation($"Dish with Id: {Id} deleting...");
            
            return await _dishClient.DeleteDish( new Dish{Id=Id});

        }
    }
}
