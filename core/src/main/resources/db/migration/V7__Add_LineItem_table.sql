-- Create a table for line items (payments and charges)

CREATE TABLE line_item (
    id SERIAL PRIMARY KEY,
    school_id INTEGER NOT NULL REFERENCES school(id),
    amount DECIMAL NOT NULL,
    name TEXT NOT NULL,
    memo TEXT,
    timestamp TIMESTAMP NOT NULL
);