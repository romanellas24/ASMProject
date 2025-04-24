using System.Collections.Generic;
using acmeat.server.user;

namespace acmeat.server.user.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public interface UserDao
  {
    public User GetUserById(int UserId);
    public List<User> GetUsers();
  }