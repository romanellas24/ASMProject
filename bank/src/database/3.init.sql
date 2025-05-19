ALTER TABLE tbl_token_transactions
    ADD `deletable` INT DEFAULT 1;

create view view_accounts_transactions as
SELECT token,
       amount,
       src_account,
       dest_account,
       payment_request_time,
       transaction_on,
       src.owner  AS src_owner,
       dest.owner AS dest_owner,
       deletable
FROM tbl_tokens
         NATURAL JOIN tbl_token_transactions
         NATURAL JOIN
         (SELECT PAN AS card, refer_account AS src_account FROM tbl_debit_cards) AS cards
         INNER JOIN tbl_account AS src ON (src.id = src_account)
         INNER JOIN tbl_account AS dest ON (dest.id = tbl_tokens.dest_account)
ORDER BY transaction_on DESC;