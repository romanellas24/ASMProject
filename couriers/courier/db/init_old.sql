CREATE DATABASE IF NOT EXISTS courier_db;

USE courier_db;

CREATE TABLE IF NOT EXISTS vehicle (
    id INT NOT NULL AUTO_INCREMENT,
    available BOOLEAN NOT NULL default 1,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS company (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(24) NOT NULL,
    hash VARCHAR(64) AS (SHA2(CONCAT(name, "=J/PYC%Z,DBQ/p8M{Z4GiDjC7/h:=QiY!8GRfKmX2)LT8eDEh+L3c({fqx%X&36d"), 256)) STORED NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders ( 
    vehicle_id INT NOT NULL, 
    order_id INT NOT NULL AUTO_INCREMENT, 
    company_id INT NOT NULL,
    start_delivery_time DATETIME, 
    end_delivery_time DATETIME, 
    PRIMARY KEY (order_id), 
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    FOREIGN KEY (company_id) REFERENCES company(id)
);


/*
    Populate DB
*/

INSERT INTO vehicle  VALUES (),(),(),(),(),(),(),();

INSERT INTO company (name) VALUES ("ACMEat"),("Deliverboh")