#!/bin/bash

# Get secret for hashing
if [ ! -f .env ]; then
  echo ".env file not found!"
  exit 1
fi

set -a
source .env
set +a

if [ -z "$HASH_SECRET" ]; then
  echo "HASH_SECRET is not set in .env"
  exit 1
fi

# Dichiarazione aziende
companies=("ACMEat" "Deliverboh" "MangiaExpress" "FudRocket")

> init.sql
> pass.env

# Scrivi tutto direttamente in init.sql
cat <<EOF > init.sql
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
EOF


for name in "${companies[@]}"; do
  hash=$(echo -n "${name}${HASH_SECRET}" | sha256sum | awk '{print $1}')
  echo "INSERT INTO company (name, hash) VALUES (\"$name\", \"$hash\");" >> init.sql
  echo "${name}_HASH=$hash" >> pass.env
done

echo "File init.sql e pass.env generati con successo."