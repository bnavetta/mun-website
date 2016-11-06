CREATE TABLE committee (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	description TEXT,
	committee_type VARCHAR(20)
);

CREATE INDEX committee_name ON committee (name);

CREATE TABLE school (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE delegate (
	id SERIAL PRIMARY KEY,
	name TEXT,
	school_id INTEGER REFERENCES school (id)
);

CREATE TABLE position (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	committee_id INTEGER NOT NULL REFERENCES committee (id),
	delegate_id INTEGER REFERENCES delegate (id)
);

CREATE INDEX position_name ON position (name);