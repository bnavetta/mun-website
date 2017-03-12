-- Table for password reset codes

CREATE TABLE password_reset_token (
    reset_code TEXT PRIMARY KEY,
    advisor_id INTEGER REFERENCES advisor(id) NOT NULL,
    requested_at TIMESTAMP NOT NULL
);