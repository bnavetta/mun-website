CREATE TABLE hotel(
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL
);

ALTER TABLE school
	ADD COLUMN status VARCHAR(20) DEFAULT 'REGISTERED',
	ADD COLUMN phone_number TEXT,
	ADD COLUMN number_of_years_attended INTEGER,
	ADD COLUMN years_attended TEXT,
	ADD COLUMN about_text TEXT,
	ADD COLUMN registration_time TIMESTAMP,
	ADD COLUMN hotel_id INTEGER REFERENCES hotel (id), -- Logistics fields
	ADD COLUMN commuting BOOLEAN,
	ADD COLUMN using_shuttle BOOLEAN,
	ADD COLUMN country TEXT,                           -- Address fields
	ADD COLUMN street_address TEXT,
	ADD COLUMN city TEXT,
	ADD COLUMN state TEXT,
	ADD COLUMN postal_code TEXT;
