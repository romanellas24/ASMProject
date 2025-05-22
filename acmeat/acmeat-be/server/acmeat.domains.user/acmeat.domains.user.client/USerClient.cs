using System.Net.Http;
using System.Threading.Tasks;
using acmeat.server.order.client;
using Grpc.Net.Client;
using Microsoft.Extensions.Options;
//PUBLISH NEW VERSIONS ONCE CONFIGURATION IS WORKING
namespace acmeat.server.user.client
{

    public class UserClient
    {
        private readonly HttpClientHandler _httpClientHandler;

        private GrpcChannel _channel;
        private GrpcUser.GrpcUserClient _client;
        private readonly UserClientOptions _options;


        public UserClient(IOptions<UserClientOptions>  options


            )
        {
            _options = options.Value;
            _httpClientHandler = new HttpClientHandler();
            _httpClientHandler.ServerCertificateCustomValidationCallback = HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

            _channel = GrpcChannel.ForAddress(_options.UserManagerConnectionString, new GrpcChannelOptions { HttpHandler = _httpClientHandler });



            _client = new GrpcUser.GrpcUserClient(_channel);

        }
        public async Task<User> GetUserById(int id)
        {
            Id id1 = new Id();
            id1.Id_ = id;
            return await _client.GetUserByIdAsync(id1);
        }

        public async Task<User> GetUserByMail(string mail)
        {
            Mail Mail1 = new Mail();
            Mail1.Mail_ = mail;
            return await _client.GetUserByMailAsync(Mail1);
        }


        public async Task<UserList> GetUserList()
        {
            Id id1 = new Id();
            id1.Id_ = 0;
            return await _client.GetUsersAsync(id1);
        }

        public async Task<GeneralResponse> CreateUser(User user)
        {


            return await _client.CreateUserAsync(user);
        }

        public async Task<GeneralResponse> UpdateUser(User user)
        {

            return await _client.UpdateUserAsync(user);
        }

        public async Task<GeneralResponse> DeleteUser(User user)
        {
            return await _client.DeleteUserAsync(user);
        }
    }
}