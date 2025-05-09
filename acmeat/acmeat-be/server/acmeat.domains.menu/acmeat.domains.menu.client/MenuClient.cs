using System.Net.Http;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.menu.client
{

    public class MenuClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcMenu.GrpcMenuClient _client;
        private readonly MenuClientOptions _options;


        public MenuClient(IOptions<MenuClientOptions>  options


            )
        {
            _options = options.Value;
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress(_options.MenuManagerConnectionString, new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcMenu.GrpcMenuClient(_channel);

        }
        public async Task<Menu> GetMenuById(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetMenuByIdAsync(id1);
        }

        public async Task<MenuList> GetMenuByLocalId(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetMenusByLocalIdAsync(id1);
        }

        public async Task<MenuList> GetMenuList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetMenusAsync(id1);
        }

        public async Task<GeneralResponse> CreateMenu(Menu menu)
        {


            return await _client.CreateMenuAsync(menu);
        }

        public async Task<GeneralResponse> UpdateMenu(Menu menu)
        {

            return await _client.UpdateMenuAsync(menu);
        }

          public async Task<GeneralResponse> UpdateMenus(MenuList menus)
        {
            return await _client.UpdateMenusAsync(menus);     
        }

        public async Task<GeneralResponse> DeleteMenu(Menu menu)
        {
            return await _client.DeleteMenuAsync(menu);
        }
    }
}