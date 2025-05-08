
using acmeat.server.menu.client;
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.menu
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class MenuController : ControllerBase
    {

        private readonly MenuClient _menuClient;
        private readonly ILogger<MenuController> _logger;

        public MenuController(
            MenuClient menuClient,
            ILogger<MenuController> logger

         ){

            _logger = logger;
            _menuClient = menuClient;
            
        }


        [HttpGet("{Id}")]
        public async Task<MenuInfo> GetMenuById(int Id)
        {
            _logger.LogInformation($"Getting menu with id: {Id}");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var menu = await _menuClient.GetMenuById(Id);
            return new MenuInfo(menu);

        }

         [HttpGet("{LocalId}")]
        public async Task<List<MenuInfo>> GetMenusByLocalId(int LocalId)
        {
            _logger.LogInformation($"Getting menus from Local with Id: {LocalId}");


            var menus = await _menuClient.GetMenuByLocalId(LocalId);
            return menus.Menus.Select(x => new MenuInfo(x)).ToList();

        }

        [HttpGet]
        public async Task<List<MenuInfo>> GetMenus()
        {
            _logger.LogInformation($"Getting menus ");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var menus = await _menuClient.GetMenuList();
            return menus.Menus.Select(x => new MenuInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateMenu(MenuInfo menuInfo)
        {
            Console.WriteLine($"Menu with made with menuId: {menuInfo.Id}");
            
            return await _menuClient.CreateMenu(menuInfo.Convert());

        }

         [HttpPatch]
        public async Task<GeneralResponse> UpdateMenu(MenuInfo menuInfo)
        {
            Console.WriteLine($"Menu with Id: {menuInfo.Id} updating...");
            
            return await _menuClient.UpdateMenu(menuInfo.Convert());

        }


         [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteMenuById(int Id)
        {
            Console.WriteLine($"Menu with Id: {Id} deleting...");
            
            return await _menuClient.DeleteMenu( new Menu{Id=Id});

        }
    }
}
