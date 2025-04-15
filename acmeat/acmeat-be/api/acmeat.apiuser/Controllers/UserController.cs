
using Microsoft.AspNetCore.Mvc;

namespace acmeat.api.user
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class UserController : ControllerBase
    {

        [HttpGet("{Id}")]
        public UserInfo GetUserById(Guid Id){
            Console.WriteLine($"user id: {Id}");
            return new UserInfo("Via Spatolini 11,Bologna","pipponzio123@gmail.com","sadjaskjdjasjdoasoidiasdoiasd");

        }
    }
}
