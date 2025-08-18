//account, check_result
define checkAccount 
{
    scope ( checkMoneyScope ) {
        sql_cmd = "SELECT COUNT(*) AS cnt FROM tbl_account WHERE id = :account";
        install ( SQLException =>
            println@Console("---------------------------------")();
            println@Console("Failed Query: ")();
            println@Console(sql_cmd)();
            println@Console("account:" + account)();
            println@Console(checkMoneyScope.SQLException.Error)();
            println@Console("---------------------------------")()
        );
        queryRequest = sql_cmd;
        queryRequest.account = account;
        query@Database( queryRequest )( queryReturn );
        if(#queryReturn.row > 0){
            if(queryReturn.row[0].cnt != 0){
                check_result = queryReturn.row[0].cnt
            }
        }
    }
}