DELIMITER $$
create
    definer = romanellas@`%` function CREATE_ACCOUNT(own varchar(768)) returns int deterministic
BEGIN
    DECLARE ACCOUNT_IDENTIFIER INT DEFAULT 1;
    SELECT (IF(MAX(id) IS NULL, 0, MAX(id))) + 1 from tbl_account INTO ACCOUNT_IDENTIFIER;
    INSERT INTO tbl_account(id, owner) VALUES (ACCOUNT_IDENTIFIER, own);
    RETURN ACCOUNT_IDENTIFIER;
END
$$
DELIMITER ;

DELIMITER $$
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
END
$$
DELIMITER ;