--- Table for storing password reset tokens

CREATE TABLE password_reset (
  reset_code TEXT PRIMARY KEY,
  advisor_id INTEGER REFERENCES advisor,
  requested_at TIMESTAMPTZ
);
