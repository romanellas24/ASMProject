using acmeat.db.user;
using acmeat.db.order;
using Microsoft.AspNetCore.Mvc;


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



    }
}
