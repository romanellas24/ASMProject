

using acmeat.db.mysql;
using acmeat.db.user;
using acmeat.server.Order;

public class OrderClient{
    
    public MysqlClient _mysqlClient;

    public OrderClient(ILogger<OrderClient> logger,MysqlClient mysqlClient){
        _mysqlClient = mysqlClient;
    }
    // TO DO: TEST IF IT WORKS 
    public List<User>GetOrders(){
       return _mysqlClient.GetUsers();
    }
}