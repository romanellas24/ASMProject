START TRANSACTION;
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

create table tbl_transactions
(
    transaction_id varchar(24)                        not null comment 'AAAA-BBBB-CCCC-DDDD-EEEE'
        primary key,
    card           varchar(16)                        not null,
    amount         decimal(20, 10)                    not null,
    dest_account   int                                null,
    transaction_on datetime default CURRENT_TIMESTAMP not null,
    constraint tbl_transactions_tbl_account_id_fk
        foreign key (dest_account) references tbl_account (id),
    constraint tbl_transactions_tbl_debit_cards_PAN_fk
        foreign key (card) references tbl_debit_cards (PAN)
            on update cascade on delete cascade
);

create  view view_balances as
select `asm_developing`.`view_transaction_list`.`account`              AS `account`,
       sum(`asm_developing`.`view_transaction_list`.`variation_value`) AS `balance`
from `asm_developing`.`view_transaction_list`
group by `asm_developing`.`view_transaction_list`.`account`;

create  view view_transaction_list as
select `asm_developing`.`tbl_deposit`.`account`       AS `account`,
       `asm_developing`.`tbl_deposit`.`deposit_value` AS `variation_value`,
       `asm_developing`.`tbl_deposit`.`deposit_on`    AS `deposit_on`
from `asm_developing`.`tbl_deposit`
union
select `asm_developing`.`tbl_debit_cards`.`refer_account`    AS `account`,
       (-(1) * `asm_developing`.`tbl_transactions`.`amount`) AS `variation_value`,
       `asm_developing`.`tbl_transactions`.`transaction_on`  AS `deposit_on`
from (`asm_developing`.`tbl_transactions` join `asm_developing`.`tbl_debit_cards`
      on ((`asm_developing`.`tbl_transactions`.`card` = `asm_developing`.`tbl_debit_cards`.`PAN`)))
union
select `asm_developing`.`tbl_transactions`.`dest_account`   AS `account`,
       `asm_developing`.`tbl_transactions`.`amount`         AS `variation_value`,
       `asm_developing`.`tbl_transactions`.`transaction_on` AS `deposit_on`
from `asm_developing`.`tbl_transactions`;

COMMIT;