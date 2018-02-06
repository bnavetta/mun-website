CREATE TABLE advisor (
    id SERIAL PRIMARY KEY,
    is_primary BOOLEAN,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    phone_number TEXT NOT NULL,
    password TEXT NOT NULL,
    school_id INTEGER REFERENCES school (id)
);