CREATE TABLE clients (
    id          VARCHAR(50)  PRIMARY KEY,
    full_name   VARCHAR(255) NOT NULL,
    inn         VARCHAR(12)  NOT NULL UNIQUE
);

CREATE TABLE client_deals (
    deal_id   VARCHAR(50) PRIMARY KEY,
    client_id VARCHAR(50) NOT NULL,
    CONSTRAINT fk_client_deals_client
        FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);