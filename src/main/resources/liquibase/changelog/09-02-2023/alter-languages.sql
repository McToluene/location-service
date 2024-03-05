ALTER TABLE languages
    ALTER COLUMN id SET DEFAULT uuid_generate_v4(),
    ALTER COLUMN id SET NOT NULL;
