include "BankGatewayInterface.iol"

include "console.iol"
include "string_utils.iol"
include "database.iol"

execution{ concurrent }



init {
    // connect to DB

    with ( connectionInfo ) {
        .username = "romanellas";
        .password = "59741404";
        .host = "localhost";
        .database = "jolie_bank";
        .driver = "mysql"
    };
    connect@Database( connectionInfo )( void );

    scope (createAccountTable) {
        install (SQLException => println@Console("SqlException: Mysql Error")());
        update@Database(
            "create table IF NOT EXISTS tbl_account(id    int auto_increment primary key, owner varchar(768) null)"
        )(ret)
        println@Console("CREATED table tbl_account with result: " + ret)()
    }
    scope (createCardsTable) {
        install (SQLException => println@Console("TodoItem table already there")());
        update@Database(
            "create table IF NOT EXISTS tbl_debit_cards ( PAN           varchar(16) not null primary key, cvv           smallint    not null, expiry_on     date        not null, refer_account int         not null, constraint tbl_debit_cards_tbl_account_id_fk foreign key (refer_account) references tbl_account (id) on update cascade on delete cascade, constraint check_cvv check (`cvv` between 0 and 999))"
        )(ret)
        println@Console("CREATED table tbl_debit_cards with result: " + ret)()
    }
    scope (createTransactionTable) {
        install (SQLException => println@Console("SqlException: Mysql Error")());
        update@Database(
            "create table IF NOT EXISTS tbl_transactions ( transaction_id varchar(24)     not null comment 'AAAA-BBBB-CCCC-DDDD-EEEE' primary key, card           varchar(16)     not null, amount         decimal(10, 10) not null, dest_account   int             null, constraint tbl_transactions_tbl_account_id_fk foreign key (dest_account) references tbl_account (id), constraint tbl_transactions_tbl_debit_cards_PAN_fk foreign key (card) references tbl_debit_cards (PAN) on update cascade on delete cascade)"
        )(ret)
        println@Console("CREATED table tbl_transactions with result: " + ret)()
    }
    
    println@Console("Bank is running...")()
}

main {
    [ getCheckPay( request )( response ) {
        payRequest = {}
        payRequest.param.transactionId = request.transactionId
        payRequest.param.transactionId = "AAAAAAAAAAAAAAAAAA"
        getCheckPay@BankPaymentsPort(payRequest)(resPay)
        response.amount = resPay.param.amount
        response.status = resPay.param.status
    }]

    [ postPay( request )( response ) {
        response.status = "Ok"
    }]

    [deleteRefund(request)(response) {
        response.status = "Ok"
    }]
}