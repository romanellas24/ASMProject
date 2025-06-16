

using System.Collections.Generic;
using System.Linq;
namespace acmeat.server.user;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public static class Utils{

    public static User ConvertDbElementToServerElement(db.user.User user){
        return new User(user);
    }


    public static List<User> ConvertDbListToServerList(List<db.user.User> users){
          return users.Select(ConvertDbElementToServerElement).ToList();
    }

    public static db.user.User ConvertServerElementIntoDbELement(User user){
        db.user.User userDB = new db.user.User(user.Id,user.Address,user.Mail,user.Pwd);

        userDB.Id = user.Id;

        return userDB;
    }
}