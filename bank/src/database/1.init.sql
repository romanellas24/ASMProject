CREATE USER `romanellas`@`%` IDENTIFIED BY '59741404';
GRANT ALL PRIVILEGES ON *.* TO romanellas@`%` WITH GRANT OPTION;

create table tbl_account
(
    id    int auto_increment
        primary key,
    owner varchar(768) null
);

--
-- Dumping data for table `tbl_account`
--

LOCK TABLES `tbl_account` WRITE;
/*!40000 ALTER TABLE `tbl_account` DISABLE KEYS */;
INSERT INTO `tbl_account` VALUES (1,'Daniele Romanella'),(2,'Elisa Rossi');
/*!40000 ALTER TABLE `tbl_account` ENABLE KEYS */;
UNLOCK TABLES;

create table tbl_debit_cards
(
    PAN           varchar(16) not null
        primary key,
    cvv           smallint    not null,
    expiry_on     date        not null,
    refer_account int         not null,
    constraint tbl_debit_cards_tbl_account_id_fk
        foreign key (refer_account) references tbl_account (id)
            on update cascade on delete cascade,
    constraint check_cvv
        check (`cvv` between 0 and 999)
);

--
-- Dumping data for table `tbl_debit_cards`
--

LOCK TABLES `tbl_debit_cards` WRITE;
/*!40000 ALTER TABLE `tbl_debit_cards` DISABLE KEYS */;
INSERT INTO `tbl_debit_cards` VALUES ('5353530123456789',123,'2025-05-31',1);
/*!40000 ALTER TABLE `tbl_debit_cards` ENABLE KEYS */;
UNLOCK TABLES;

create table tbl_deposit
(
    deposit_no    bigint auto_increment
        primary key,
    deposit_value decimal(20, 10) default 0.0000000000      null,
    account       int                                       null,
    deposit_on    datetime        default CURRENT_TIMESTAMP not null,
    constraint tbl_deposit_tbl_account_id_fk
        foreign key (account) references tbl_account (id)
)
    comment 'General table for deposits';

--
-- Dumping data for table `tbl_deposit`
--

LOCK TABLES `tbl_deposit` WRITE;
/*!40000 ALTER TABLE `tbl_deposit` DISABLE KEYS */;
INSERT INTO `tbl_deposit` VALUES (1,100000.0000000000,1,'2025-04-16 19:28:42');
/*!40000 ALTER TABLE `tbl_deposit` ENABLE KEYS */;
UNLOCK TABLES;

create table tbl_token_transactions
(
    token          varchar(96)                        not null
        primary key,
    card           varchar(16)                        not null,
    transaction_on datetime default CURRENT_TIMESTAMP not null,
    constraint tbl_token_transactions_tbl_debit_cards_PAN_fk
        foreign key (card) references tbl_debit_cards (PAN)
            on update cascade on delete cascade
);

create table tbl_tokens
(
    token                varchar(96)                               not null,
    payment_request_time datetime        default CURRENT_TIMESTAMP not null,
    amount               decimal(20, 10) default 0.0000000000      not null,
    dest_account         int                                       null,
    constraint tbl_tokens_pk
        unique (token),
    constraint tbl_tokens_tbl_account_id_fk
        foreign key (dest_account) references tbl_account (id)
);

create definer = romanellas@`%` view view_transaction_list as
select `jolie_bank`.`tbl_deposit`.`account`       AS `account`,
       `jolie_bank`.`tbl_deposit`.`deposit_value` AS `variation_value`,
       `jolie_bank`.`tbl_deposit`.`deposit_on`    AS `deposit_on`
from `jolie_bank`.`tbl_deposit`
union
select `jolie_bank`.`tbl_debit_cards`.`refer_account`         AS `account`,
       (-(1) * `tkn`.`amount`)                                AS `variation_value`,
       `jolie_bank`.`tbl_token_transactions`.`transaction_on` AS `deposit_on`
from ((`jolie_bank`.`tbl_token_transactions` join `jolie_bank`.`tbl_debit_cards`
       on ((`jolie_bank`.`tbl_token_transactions`.`card` =
            `jolie_bank`.`tbl_debit_cards`.`PAN`))) join `jolie_bank`.`tbl_tokens` `tkn`
      on ((`tkn`.`token` = `jolie_bank`.`tbl_token_transactions`.`token`)))
union
select `jolie_bank`.`tbl_tokens`.`dest_account`               AS `account`,
       `jolie_bank`.`tbl_tokens`.`amount`                     AS `variation_value`,
       `jolie_bank`.`tbl_token_transactions`.`transaction_on` AS `deposit_on`
from (`jolie_bank`.`tbl_token_transactions` join `jolie_bank`.`tbl_tokens`
      on ((`jolie_bank`.`tbl_token_transactions`.`token` = `jolie_bank`.`tbl_tokens`.`token`)))
order by `deposit_on` desc, `variation_value` desc;

create definer = romanellas@`%` view view_balances as
select `jolie_bank`.`tbl_account`.`id`                                          AS `account`,
       coalesce(sum(`jolie_bank`.`view_transaction_list`.`variation_value`), 0) AS `balance`
from (`jolie_bank`.`tbl_account` left join `jolie_bank`.`view_transaction_list`
      on ((`jolie_bank`.`tbl_account`.`id` = `jolie_bank`.`view_transaction_list`.`account`)))
group by `jolie_bank`.`tbl_account`.`id`;