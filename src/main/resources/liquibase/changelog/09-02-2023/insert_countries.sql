BEGIN;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DELETE FROM countries;

INSERT INTO countries ("id","public_id", "country_name", "created_by", "created_date" ,"status", "two_letter_code", "three_letter_code", "dialing_code", "last_modified_by", "last_modified_date", "preferred_state_name", "preferred_city_name", "preferred_lga_name")
VALUES (uuid_generate_v4(), uuid_generate_v4(), 'Nigeria', 'SYSTEM', '2023-01-15 17:15:08.648625', 'INACTIVE', 'NG', 'NGA', '234', 'SYSTEM', '2023-01-15 17:15:08.648625', 'STATE', 'CITY', 'LGA');

INSERT INTO countries ("id","public_id", "country_name", "created_by", "created_date" ,"status", "two_letter_code", "three_letter_code", "dialing_code", "last_modified_by", "last_modified_date", "preferred_state_name", "preferred_city_name", "preferred_lga_name")
VALUES (uuid_generate_v4(), uuid_generate_v4(), 'Kenya', 'SYSTEM', '2023-01-15 17:15:08.648625', 'INACTIVE', 'KE', 'KEN', '254', 'SYSTEM', '2023-01-15 17:15:08.648625', 'PROVINCE', 'DISTRICTS', 'LGA');
END;