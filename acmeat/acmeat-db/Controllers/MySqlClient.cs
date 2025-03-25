using acmeat.db.user;
using acmeat.db.order;
using Microsoft.AspNetCore.Mvc;
using acmeat.db.menu;
using acmeat.db.local;
using acmeat.db.deliveryCompany;


namespace acmeat.db.mysql
{
    [Route("api/[controller]/[action]")]
    [ApiController]


    public class MysqlClient(ILogger<MysqlClient> logger,
                             MySqlContext context) : ControllerBase
    {

        private readonly ILogger<MysqlClient> _logger = logger;
        private readonly UserContext _UserContext = new UserContext(context);
        private readonly OrderContext _OrderContext = new OrderContext(context);
        private readonly MenuContext _MenuContext = new MenuContext(context);
        private readonly LocalContext _LocalContext = new LocalContext(context);
        private readonly DeliveryCompanyContext _DeliveryCompanyContext = new DeliveryCompanyContext(context);

        #region User
        [HttpGet]
        public List<User> GetUsers()
        {
            _logger.LogInformation("Getting users");
            return _UserContext.GetUsers();
        }

        [HttpGet("{id}")]

        public User GetUserById(int id)
        {
            _logger.LogInformation($"Getting information for user with id {id}");
            return _UserContext.GetUserById(id);
        }

        [HttpPost]
        public async Task CreateUser(User user)
        {
            _logger.LogInformation($"Creating User: {user.Mail}");
            await _UserContext.CreateUser(user);
        }

        [HttpPatch]
        public async Task UpdateUser(User user)
        {
            _logger.LogInformation($"Updating user with Id: {user.Id}" );
            await _UserContext.UpdateUser(user);
        }

        [HttpDelete]
        public async Task DeleteUser(User user)
        {
            _logger.LogInformation($"Deleting User with Id: {user.Id}");
            await _UserContext.DeleteUser(user);
        }

        [HttpDelete("{id}")]
        public async Task DeleteUserById(int id)
        {
            _logger.LogInformation($"Deleting User with Id: {id}");
            await _UserContext.DeleteUserById(id);
        }

        #endregion

        #region Order
        [HttpGet]
        public List<Order> GetOrders()
        {
            _logger.LogInformation("Getting Orders");
            return _OrderContext.GetOrders();
        }
         [HttpGet("{id}")]
        public Order GetOrderById(int id)
        {
            _logger.LogInformation($"Getting information for Order with id {id}");
            return _OrderContext.GetOrderById(id);
        }
        [HttpPost]
        public async Task CreateOrder(Order Order)
        {
            _logger.LogInformation($"Creating Order with transaction id: {Order.TransactionId}");
            await _OrderContext.CreateOrder(Order);
        }

        [HttpPatch]
        public async Task UpdateOrder(Order Order)
        {
            _logger.LogInformation($"Updating Order with Id: {Order.Id}" );
            await _OrderContext.UpdateOrder(Order);
        }

        [HttpDelete]
        public async Task DeleteOrder(Order Order)
        {
            _logger.LogInformation($"Deleting Order with Id: {Order.Id}");
            await _OrderContext.DeleteOrder(Order);
        }
        #endregion
        

        #region Menu
        [HttpGet]
        public List<Menu> GetMenus()
        {
            _logger.LogInformation("Getting Menus");
            return _MenuContext.GetMenus();
        }
         [HttpGet("{id}")]
        public Menu GetMenuById(int id)
        {
            _logger.LogInformation($"Getting information for Menu with id {id}");
            return _MenuContext.GetMenuById(id);
        }
        [HttpPost]
        public async Task CreateMenu(Menu Menu)
        {
            _logger.LogInformation($"Creating Menu with id: {Menu.Id}");
            await _MenuContext.CreateMenu(Menu);
        }

        [HttpPatch]
        public async Task UpdateMenu(Menu Menu)
        {
            _logger.LogInformation($"Updating Menu with Id: {Menu.Id}" );
            await _MenuContext.UpdateMenu(Menu);
        }

        [HttpDelete]
        public async Task DeleteMenu(Menu Menu)
        {
            _logger.LogInformation($"Deleting Menu with Id: {Menu.Id}");
            await _MenuContext.DeleteMenu(Menu);
        }
        #endregion

         #region Local
        [HttpGet]
        public List<Local> GetLocals()
        {
            _logger.LogInformation("Getting Locals");
            return _LocalContext.GetLocals();
        }
         [HttpGet("{id}")]
        public Local GetLocalById(int id)
        {
            _logger.LogInformation($"Getting information for Local with id {id}");
            return _LocalContext.GetLocalById(id);
        }
        [HttpPost]
        public async Task CreateLocal(Local Local)
        {
            _logger.LogInformation($"Creating Local with id: {Local.Id}");
            await _LocalContext.CreateLocal(Local);
        }

        [HttpPatch]
        public async Task UpdateLocal(Local Local)
        {
            _logger.LogInformation($"Updating Local with Id: {Local.Id}" );
            await _LocalContext.UpdateLocal(Local);
        }

        [HttpDelete]
        public async Task DeleteLocal(Local Local)
        {
            _logger.LogInformation($"Deleting Local with Id: {Local.Id}");
            await _LocalContext.DeleteLocal(Local);
        }
        #endregion

         #region DeliveryCompany
        [HttpGet]
        public List<DeliveryCompany> GetDeliveryCompanys()
        {
            _logger.LogInformation("Getting DeliveryCompanys");
            return _DeliveryCompanyContext.GetDeliveryCompanies();
        }
         [HttpGet("{id}")]
        public DeliveryCompany GetDeliveryCompanyById(int id)
        {
            _logger.LogInformation($"Getting information for DeliveryCompany with id {id}");
            return _DeliveryCompanyContext.GetDeliveryCompanyById(id);
        }
        [HttpPost]
        public async Task CreateDeliveryCompany(DeliveryCompany DeliveryCompany)
        {
            _logger.LogInformation($"Creating DeliveryCompany with id: {DeliveryCompany.Id}");
            await _DeliveryCompanyContext.CreateDeliveryCompany(DeliveryCompany);
        }

        [HttpPatch]
        public async Task UpdateDeliveryCompany(DeliveryCompany DeliveryCompany)
        {
            _logger.LogInformation($"Updating DeliveryCompany with Id: {DeliveryCompany.Id}" );
            await _DeliveryCompanyContext.UpdateDeliveryCompany(DeliveryCompany);
        }

        [HttpDelete]
        public async Task DeleteDeliveryCompany(DeliveryCompany DeliveryCompany)
        {
            _logger.LogInformation($"Deleting DeliveryCompany with Id: {DeliveryCompany.Id}");
            await _DeliveryCompanyContext.DeleteDeliveryCompany(DeliveryCompany);
        }
        #endregion



    }
}
