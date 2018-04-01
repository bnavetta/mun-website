-- Add the hotels table (forgot it initially)

CREATE TABLE hotel (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  rate INTEGER,
  address TEXT,
  phone TEXT,
  notes TEXT
);