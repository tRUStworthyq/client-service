CREATE OR REPLACE VIEW vw_clients AS
    SELECT id, full_name, inn FROM clients;

CREATE OR REPLACE VIEW vw_client_deals AS
    SELECT deal_id, client_id FROM client_deals;
