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

create table tbl_tokens
(
    id                   int auto_increment
        primary key,
    payment_request_time datetime default CURRENT_TIMESTAMP not null,
    amount               decimal(20, 10)                    not null,
    dest_account         int                                not null,
    token                varchar(96)                        null,
    constraint tbl_tokens_tbl_account_id_fk
        foreign key (dest_account) references tbl_account (id)
);

create table tbl_token_transactions
(
    token_id       int                                not null
        primary key,
    card           varchar(16)                        not null,
    amount         decimal(20, 10)                    not null,
    dest_account   int                                null,
    transaction_on datetime default CURRENT_TIMESTAMP not null,
    constraint tbl_token_transactions_tbl_account_id_fk
        foreign key (dest_account) references tbl_account (id),
    constraint tbl_token_transactions_tbl_debit_cards_PAN_fk
        foreign key (card) references tbl_debit_cards (PAN)
            on update cascade on delete cascade,
    constraint tbl_token_transactions_tbl_tokens_id_fk
        foreign key (token_id) references tbl_tokens (id)
);

create definer = romanellas@`%` view view_balances as
select `asm_developing`.`view_transaction_list`.`account`              AS `account`,
       sum(`asm_developing`.`view_transaction_list`.`variation_value`) AS `balance`
from `asm_developing`.`view_transaction_list`
group by `asm_developing`.`view_transaction_list`.`account`;

create definer = romanellas@`%` view view_transaction_list as
select `asm_developing`.`tbl_deposit`.`account`       AS `account`,
       `asm_developing`.`tbl_deposit`.`deposit_value` AS `variation_value`,
       `asm_developing`.`tbl_deposit`.`deposit_on`    AS `deposit_on`
from `asm_developing`.`tbl_deposit`
union
select `asm_developing`.`tbl_debit_cards`.`refer_account`          AS `account`,
       (-(1) * `asm_developing`.`tbl_token_transactions`.`amount`) AS `variation_value`,
       `asm_developing`.`tbl_token_transactions`.`transaction_on`  AS `deposit_on`
from (`asm_developing`.`tbl_token_transactions` join `asm_developing`.`tbl_debit_cards`
      on ((`asm_developing`.`tbl_token_transactions`.`card` = `asm_developing`.`tbl_debit_cards`.`PAN`)))
union
select `asm_developing`.`tbl_token_transactions`.`dest_account`   AS `account`,
       `asm_developing`.`tbl_token_transactions`.`amount`         AS `variation_value`,
       `asm_developing`.`tbl_token_transactions`.`transaction_on` AS `deposit_on`
from `asm_developing`.`tbl_token_transactions`;

create
    definer = romanellas@`%` function CREATE_TOKEN(token_id DECIMAL(20,10), ts datetime) returns char(96) deterministic
    RETURN CONCAT(SHA2((ts * token_id),256), MD5(ts * token_id));

COMMIT;