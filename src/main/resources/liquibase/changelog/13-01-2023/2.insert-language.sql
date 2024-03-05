--liquibase formatted sql
--changeset babalola.owolabi@sabi.am:100000002
--


CREATE TABLE IF NOT EXISTS public.languages
(
    id uuid NOT NULL,
    public_id uuid NOT NULL,
    name character varying(255)  NOT NULL,
    status character varying(10)  NOT NULL,
    created_date timestamp without time zone NOT NULL,
    created_by character varying(255)  NOT NULL,
    CONSTRAINT languages_pkey PRIMARY KEY (id),
    CONSTRAINT unique_languages_key UNIQUE (public_id),
    CONSTRAINT unique_languages_name_key UNIQUE (name)
    );

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Arabic', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Portuguese', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'French', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'English', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Kirundi', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Comorian', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Spanish', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Tigrinya', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Amharic', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Swahili', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Malagasy', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Mauritian Creole', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Kinyarwanda', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Seychellois Creole', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Somali', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Afrikaans', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Setswana', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Sango', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Sesotho', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Chichewa', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Berber', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Swati', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Luganda', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Shona', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Sindebele', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Southern Ndebele', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Northern Sotho', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Southern Sotho', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Tsonga', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Tswana', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Venda', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');


insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Xhosa', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');


insert into languages ("id","public_id", "name", "created_by", "created_date" ,"status")
values (uuid_generate_v4(), uuid_generate_v4(), 'Zulu', 'SYSTEM','2023-01-15 17:15:08.648625','INACTIVE');

