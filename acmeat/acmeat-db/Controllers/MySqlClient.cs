using acmeat.db.user;
using acmeat.db.order;
using acmeat.db.menu;
using acmeat.db.local;
using acmeat.db.deliveryCompany;
using acmeat.db.dish;


namespace acmeat.db.mysql
{


    public class MysqlClient(ILogger<MysqlClient> logger, IConfiguration configuration)
    {
        private readonly ILogger<MysqlClient> _logger = logger;
        private readonly UserContext _UserContext = new UserContext(configuration);
        private readonly OrderContext _OrderContext = new OrderContext(configuration);
        private readonly MenuContext _MenuContext = new MenuContext(configuration);
        private readonly LocalContext _LocalContext = new LocalContext(configuration);

        private readonly DishContext _DishContext = new DishContext(configuration);
        private readonly DeliveryCompanyContext _DeliveryCompanyContext = new DeliveryCompanyContext(configuration);

        #region User

        public List<User> GetUsers()
        {
            _logger.LogInformation("Getting users");
            return _UserContext.GetUsers();
        }



        public User GetUserById(int id)
        {
            _logger.LogInformation($"Getting information for user with id {id}");
            return _UserContext.GetUserById(id);
        }


        public async Task CreateUser(User user)
        {
            _logger.LogInformation($"Creating User: {user.Mail}");
            await _UserContext.CreateUser(user);
        }


        public async Task UpdateUser(User user)
        {
            _logger.LogInformation($"Updating user with Id: {user.Id}");
            await _UserContext.UpdateUser(user);
        }


        public async Task DeleteUser(User user)
        {
            _logger.LogInformation($"Deleting User with Id: {user.Id}");
            await _UserContext.DeleteUser(user);
        }


        public async Task DeleteUserById(int id)
        {
            _logger.LogInformation($"Deleting User with Id: {id}");
            await _UserContext.DeleteUserById(id);
        }

        #endregion

        #region Order

        public List<Order> GetOrders()
        {
            _logger.LogInformation("Getting Orders");
            return _OrderContext.GetOrders();
        }

        public Order GetOrderById(int id)
        {
            _logger.LogInformation($"Getting information for Order with id {id}");
            return _OrderContext.GetOrderById(id);
        }

        public async Task CreateOrder(Order Order)
        {
            _logger.LogInformation($"Creating Order with transaction id: {Order.TransactionId}");
            await _OrderContext.CreateOrder(Order);
        }


        public async Task UpdateOrder(Order Order)
        {
            _logger.LogInformation($"Updating Order with Id: {Order.Id}");
            await _OrderContext.UpdateOrder(Order);
        }


        public async Task DeleteOrder(Order Order)
        {
            _logger.LogInformation($"Deleting Order with Id: {Order.Id}");
            await _OrderContext.DeleteOrder(Order);
        }
        #endregion


        #region Menu

        public List<Menu> GetMenus()
        {
            _logger.LogInformation("Getting Menus");
            return _MenuContext.GetMenus();
        }

        public Menu GetMenuById(int id)
        {
            _logger.LogInformation($"Getting information for Menu with id {id}");
            return _MenuContext.GetMenuById(id);
        }

        public List<Menu> GetMenusByLocalId(int id)
        {
            _logger.LogInformation($"Getting Menus for Local with id {id}");
            return _MenuContext.GetMenusByLocalId(id);
        }

        public async Task CreateMenu(Menu Menu)
        {
            _logger.LogInformation($"Creating Menu with id: {Menu.Id}");
            await _MenuContext.CreateMenu(Menu);
        }


        public async Task UpdateMenu(Menu Menu)
        {
            _logger.LogInformation($"Updating Menu with Id: {Menu.Id}");
            await _MenuContext.UpdateMenu(Menu);
        }


        public async Task DeleteMenu(Menu Menu)
        {
            _logger.LogInformation($"Deleting Menu with Id: {Menu.Id}");
            await _MenuContext.DeleteMenu(Menu);
        }
        #endregion

        #region Local

        public List<Local> GetLocals()
        {
            _logger.LogInformation("Getting Locals");
            return _LocalContext.GetLocals();
        }

        public Local GetLocalById(int id)
        {
            _logger.LogInformation($"Getting information for Local with id {id}");
            return _LocalContext.GetLocalById(id);
        }

        public async Task CreateLocal(Local Local)
        {
            _logger.LogInformation($"Creating Local with id: {Local.Id}");
            await _LocalContext.CreateLocal(Local);
        }


        public async Task UpdateLocal(Local Local)
        {
            _logger.LogInformation($"Updating Local with Id: {Local.Id}");
            await _LocalContext.UpdateLocal(Local);
        }


        public async Task DeleteLocal(Local Local)
        {
            _logger.LogInformation($"Deleting Local with Id: {Local.Id}");
            await _LocalContext.DeleteLocal(Local);
        }
        #endregion

        #region DeliveryCompany

        public List<DeliveryCompany> GetDeliveryCompanys()
        {
            _logger.LogInformation("Getting DeliveryCompanys");
            return _DeliveryCompanyContext.GetDeliveryCompanies();
        }

        public DeliveryCompany GetDeliveryCompanyById(int id)
        {
            _logger.LogInformation($"Getting information for DeliveryCompany with id {id}");
            return _DeliveryCompanyContext.GetDeliveryCompanyById(id);
        }

        public async Task CreateDeliveryCompany(DeliveryCompany DeliveryCompany)
        {
            _logger.LogInformation($"Creating DeliveryCompany with id: {DeliveryCompany.Id}");
            await _DeliveryCompanyContext.CreateDeliveryCompany(DeliveryCompany);
        }


        public async Task UpdateDeliveryCompany(DeliveryCompany DeliveryCompany)
        {
            _logger.LogInformation($"Updating DeliveryCompany with Id: {DeliveryCompany.Id}");
            await _DeliveryCompanyContext.UpdateDeliveryCompany(DeliveryCompany);
        }


        public async Task DeleteDeliveryCompany(DeliveryCompany DeliveryCompany)
        {
            _logger.LogInformation($"Deleting DeliveryCompany with Id: {DeliveryCompany.Id}");
            await _DeliveryCompanyContext.DeleteDeliveryCompany(DeliveryCompany);
        }
        #endregion
        

         #region Dish

        public List<Dish> GetDishs()
        {
            _logger.LogInformation("Getting Dishs");
            return _DishContext.GetDishs();
        }

        public Dish GetDishById(int id)
        {
            _logger.LogInformation($"Getting information for Dish with id {id}");
            return _DishContext.GetDishById(id);
        }

        public List<Dish> GetDishsByMenuId(int id)
        {
            _logger.LogInformation($"Getting Dishs by menu with {id}");
            return _DishContext.GetDishsByMenuId(id);
        }


        public List<Dish> GetDishsByDate(string date)
        {
            _logger.LogInformation($"Getting Dishes for date {date}");
            return _DishContext.GetDishsByDate(date);
        }

        public async Task CreateDish(Dish Dish)
        {
            _logger.LogInformation($"Creating Dish with id: {Dish.Id}");
            await _DishContext.CreateDish(Dish);
        }


        public async Task UpdateDish(Dish Dish)
        {
            _logger.LogInformation($"Updating Dish with Id: {Dish.Id}");
            await _DishContext.UpdateDish(Dish);
        }


        public async Task DeleteDish(Dish Dish)
        {
            _logger.LogInformation($"Deleting Dish with Id: {Dish.Id}");
            await _DishContext.DeleteDish(Dish);
        }
        #endregion


        



    }
}
