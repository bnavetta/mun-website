-- Table for tokens when adding advisors
CREATE TABLE new_advisor_token (
    token TEXT PRIMARY KEY,
    school_id INTEGER NOT NULL REFERENCES school(id),
    advisor_name TEXT NOT NULL,
    advisor_email TEXT NOT NULL
);