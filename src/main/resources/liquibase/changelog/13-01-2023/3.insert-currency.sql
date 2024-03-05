--liquibase formatted sql
--changeset babalola.owolabi@sabi.am:100000003
--


CREATE TABLE IF NOT EXISTS public.currencies
(
    id uuid NOT NULL,
    public_id uuid NOT NULL,
    name character varying(255)  NOT NULL,
    code character varying(5) NOT NULL,
    status character varying(10)  NOT NULL,
    created_date timestamp without time zone NOT NULL,
    created_by character varying(255)  NOT NULL,
    CONSTRAINT lcurrencies_pkey PRIMARY KEY (id),
    CONSTRAINT unique_currencies_key UNIQUE (public_id),
    CONSTRAINT unique_currencies_name_key UNIQUE (name)
    );

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Algerian dinar', 'DZD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Kwanza', 'AOA', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Eco', 'ECO', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Pula', 'BWP', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Burundian franc','BIF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Central AfricanCFA franc', 'XAF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Cape Verdean escudo','CVE', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Comorian franc', 'KMF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Congolese franc', 'CDF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Djiboutian franc','DJF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Egyptian pound', 'EGP', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Eritrean nakfa', 'ERN', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Ethiopian birr' ,'ETB', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Gambian dalasi','GMD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Ghana cedi', 'GHS', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Guinean franc', 'GNF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Kenyan shilling','KES', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Lesotho loti', 'LSL', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Liberian dollara', 'LRD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Libyan dinar', 'LYD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Malagasy ariary', 'MGA', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Kwacha(D)','MWK', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Ouguiya', 'MRO', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Mauritian rupee', 'MUR', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Euro', 'EUR' ,'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Moroccan dirham', 'MAD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Mozambican metical', 'MZN', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Namibian dollar', 'NAD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Naira', 'NGN', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Rwandan franc', 'RWF', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Saint Helena pound', 'SHP', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');


insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Seychellois rupee', 'SCR', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');


insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Leone', 'SLL', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Somali shilling', 'SOS', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Somaliland shillinga', 'SLSH', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'South African rand', 'ZAR', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'South Sudanese pound', 'SSP', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Sudanese pound', 'SDG', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'South African rand Swazi lilangeni', 'SZL', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Tanzanian shilling', 'TZS', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Ugandan shilling', 'UGX', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Zambian kwacha', 'ZMW', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');


insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'United States Dollar', 'USD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Pound sterling', 'POUND', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');


insert into currencies ("id","public_id", "name", "code", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), ' RTGS dollar', 'RTD', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');