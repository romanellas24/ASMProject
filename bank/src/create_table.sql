SET AUTOCOMMIT  = 0;

create table tbl_account
(
    id    int auto_increment
        primary key,
    owner varchar(768) null
);

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

create definer = romanellas@`%` view view_balances as
select `jolie_bank`.`view_transaction_list`.`account`              AS `account`,
       sum(`jolie_bank`.`view_transaction_list`.`variation_value`) AS `balance`
from `jolie_bank`.`view_transaction_list`
group by `jolie_bank`.`view_transaction_list`.`account`;

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

create
    definer = romanellas@`%` function CREATE_TOKEN(token_id decimal(20, 10), ts datetime) returns char(96) deterministic
BEGIN
 DECLARE TOKEN_FNC VARCHAR(96) DEFAULT "";
 DECLARE NUM_ROWS INT DEFAULT 0;
  SELECT CONCAT(SHA2((ts * token_id),256), MD5(ts * token_id)) INTO TOKEN_FNC;
  SELECT COUNT(*) FROM tbl_tokens WHERE token = TOKEN_FNC INTO NUM_ROWS;
  IF NUM_ROWS = 0 THEN
      INSERT INTO tbl_tokens(token) VALUE (TOKEN_FNC);
    RETURN TOKEN_FNC;
  ELSE
      RETURN CREATE_TOKEN(token_id - 0.01, ts);
    END IF;
END;



COMMIT;