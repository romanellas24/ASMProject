
using acmeat.server.user.client;
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.user
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class UserController : ControllerBase
    {

        private readonly UserClient _userClient;
        private readonly ILogger<UserController> _logger;

        public UserController(
            UserClient userClient,
            ILogger<UserController> logger

         ){

            _logger = logger;
            _userClient = userClient;
            
        }


        [HttpGet("{Id}")]
        public async Task<UserInfo> GetUserById(int Id)
        {
            _logger.LogInformation($"Getting user with id: {Id}");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var user = await _userClient.GetUserById(Id);
            return new UserInfo(user);

        }

        [HttpGet]
        public async Task<List<UserInfo>> GetUsers()
        {
            _logger.LogInformation($"Getting users ");
            //TO DO AWAIT CLIENT TO COMPLETE THE OPERATION

            var users = await _userClient.GetUserList();
            return users.Users.Select(x => new UserInfo(x)).ToList();

        }

        [HttpPost]
        public async Task<GeneralResponse> CreateUser(UserInfo userInfo)
        {
            Console.WriteLine($"User with made with userId: {userInfo.Id}");
            
            return await _userClient.CreateUser(userInfo.Convert());

        }

         [HttpPatch]
        public async Task<GeneralResponse> UpdateUser(UserInfo userInfo)
        {
            Console.WriteLine($"User with Id: {userInfo.Id} updating...");
            
            return await _userClient.UpdateUser(userInfo.Convert());

        }


         [HttpDelete("{Id}")]
        public async Task<GeneralResponse> DeleteUserById(int Id)
        {
            Console.WriteLine($"User with Id: {Id} deleting...");
            
            return await _userClient.DeleteUser( new User{Id=Id});

        }
    }
}
