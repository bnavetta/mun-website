CREATE TABLE award (
  id SERIAL PRIMARY KEY,
  committee_id INTEGER NOT NULL REFERENCES committee (id),
  position_id INTEGER REFERENCES position (id),
  award_type VARCHAR(20) NOT NULL
);
