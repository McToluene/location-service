BEGIN;


CREATE TABLE IF NOT EXISTS public.cities
(
    id uuid NOT NULL,
    public_id uuid NOT NULL,
    name character varying(255)  NOT NULL,
    status character varying(10)  NOT NULL,
    created_date timestamp without time zone NOT NULL,
    created_by character varying(255)  NOT NULL,
    last_modified_by character varying(255) ,
    last_modified_date timestamp without time zone,
    state_province_id uuid NOT NULL,
    CONSTRAINT cities_pkey PRIMARY KEY (id),
    CONSTRAINT unique_cities_key UNIQUE (public_id)
    );

CREATE TABLE IF NOT EXISTS public.countries
(
    id uuid NOT NULL,
    public_id uuid NOT NULL,
    country_name character varying(255)  NOT NULL,
    two_letter_code character varying(2)  NOT NULL,
    three_letter_code character varying(3)  NOT NULL,
    dialing_code character varying(10)  NOT NULL,
    created_by character varying(255)  NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_by character varying(255) ,
    last_modified_date timestamp without time zone,
    status character varying(10) NOT NULL,
    preferred_state_name character varying(255) ,
    preferred_city_name character varying(255) ,
    preferred_lga_name character varying(255),
    CONSTRAINT countries_pkey PRIMARY KEY (id),
    CONSTRAINT unique_country_name UNIQUE (country_name),
    CONSTRAINT unique_three_letter_code UNIQUE (three_letter_code),
    CONSTRAINT unique_two_letter_code UNIQUE (two_letter_code)
    );

CREATE TABLE IF NOT EXISTS public.lga_county
(
    id uuid NOT NULL,
    public_id uuid NOT NULL,
    city_id uuid NOT NULL,
    name character varying(255) ,
    status character varying(10) ,
    created_by character varying(255) ,
    created_date timestamp without time zone,
    last_modified_by character varying,
    last_modified_date timestamp without time zone,
    CONSTRAINT lga_county_pkey PRIMARY KEY (id),
    CONSTRAINT unique_lga_key UNIQUE (public_id),
    CONSTRAINT unique_lga_name UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS public.states_provinces
(
    id uuid NOT NULL,
    public_id uuid NOT NULL,
    country_id uuid NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(10) NOT NULL,
    capital character varying(255)  NOT NULL,
    status character varying(10)  NOT NULL,
    created_by character varying(255) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_by character varying(255) ,
    last_modified_date timestamp without time zone,
    CONSTRAINT states_provinces_pkey PRIMARY KEY (id),
    CONSTRAINT unique_state_public_id UNIQUE (public_id)
    );

ALTER TABLE IF EXISTS public.cities
    ADD CONSTRAINT fk_cities_463673_key FOREIGN KEY (state_province_id)
    REFERENCES public.states_provinces (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;
CREATE INDEX IF NOT EXISTS fki_fk_cities_463673_key
    ON public.cities(state_province_id);


ALTER TABLE IF EXISTS public.lga_county
    ADD CONSTRAINT fk_city_id_34535636_key FOREIGN KEY (city_id)
    REFERENCES public.cities (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;
CREATE INDEX IF NOT EXISTS fki_fk_lga_county_key
    ON public.lga_county(city_id);


ALTER TABLE IF EXISTS public.states_provinces
    ADD CONSTRAINT fk_state_province_key FOREIGN KEY (country_id)
    REFERENCES public.countries (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;
CREATE INDEX IF NOT EXISTS fki_fk_state_province_key
    ON public.states_provinces(country_id);

END;