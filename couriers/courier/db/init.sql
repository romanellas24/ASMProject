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
    hash VARCHAR(64) NOT NULL,
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

-- Popola i veicoli
INSERT INTO vehicle VALUES (),(),(),(),(),(),(),();
INSERT INTO company (name, hash) VALUES ("ACMEat", "6eed64279a4d6d745ab873466bcdcac28573acb423605d25d1b5b9ff95ce0b3b");
INSERT INTO company (name, hash) VALUES ("Deliverboh", "88ccca644ca8eda4e35631102661e3e6db1521736555b773799fecaf94b945ed");
INSERT INTO company (name, hash) VALUES ("MangiaExpress", "1f69866b0cce1f3d960b8d1c5e4fbb987aeaba9795e9d902aeb6ffca1f6b3862");
INSERT INTO company (name, hash) VALUES ("FudRocket", "2e4845d4ec3a209f73cb3af019f89043360ec8abdc4396b181077ff25042ee0b");
