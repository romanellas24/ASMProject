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

create table tbl_transactions
(
    transaction_id varchar(24)     not null comment 'AAAA-BBBB-CCCC-DDDD-EEEE'
        primary key,
    card           varchar(16)     not null,
    amount         decimal(10, 10) not null,
    dest_account   int             null,
    constraint tbl_transactions_tbl_account_id_fk
        foreign key (dest_account) references tbl_account (id),
    constraint tbl_transactions_tbl_debit_cards_PAN_fk
        foreign key (card) references tbl_debit_cards (PAN)
            on update cascade on delete cascade
);

