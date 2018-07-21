--- Committees

CREATE TYPE committee_type AS ENUM ('GENERAL', 'SPECIALIZED', 'CRISIS', 'JOINT_CRISIS', 'JOINT_CRISIS_ROOM');

CREATE TABLE committee (
  -- Core properties
  id SERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  short_name TEXT UNIQUE NOT NULL,
  type committee_type NOT NULL,

  -- Content
  description TEXT,
  topic1 TEXT,
  topic2 TEXT,
  topic3 TEXT,
  topic4 TEXT,

  -- Display
  map_latitude FLOAT,
  map_longitude FLOAT,
  image TEXT
);

-- Join table linking committees of type JOINT_CRISIS to
-- their rooms (type JOINT_CRISIS_ROOM)
CREATE TABLE joint_crisis_rooms (
  joint_crisis_id INTEGER REFERENCES committee,
  room_id INTEGER REFERENCES committee,
  PRIMARY KEY (joint_crisis_id, room_id)
);

-- Positions

CREATE TABLE position (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  committee_id INTEGER NOT NULL REFERENCES committee,
  UNIQUE (committee_id, name)
);

-- Schools

CREATE TABLE school (
  -- Basic info
  id SERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,

  -- Application status
  has_applied BOOLEAN DEFAULT FALSE,
  accepted BOOLEAN DEFAULT FALSE,
  registration_code TEXT NOT NULL,

  -- Application
  has_attended_before BOOLEAN,
  years_attended TEXT,
  about_school TEXT,
  about_mun_program TEXT,
  delegation_goals TEXT,
  why_applied TEXT
);

-- For seeing the application status of all schools
CREATE VIEW school_status AS
  SELECT
    s.id, s.name, s.has_applied, s.accepted, s.registration_code
  FROM school s;

-- For getting a particular school's application
CREATE VIEW school_application AS
  SELECT s.id, s.name,
    s.has_attended_before, s.years_attended,
    s.about_school, s.about_mun_program,
    s.delegation_goals, s.why_applied
  FROM school s;

CREATE TABLE advisor (
  id SERIAL PRIMARY KEY,
  name TEXT,
  email TEXT UNIQUE NOT NULL,
  phone_number TEXT,
  password TEXT,
  school_id INTEGER REFERENCES school
);

CREATE TABLE delegate (
  id SERIAL PRIMARY KEY,
  name TEXT,
  school_id INTEGER REFERENCES school,
  position_id INTEGER REFERENCES position,

  -- Attendance fields
  position_paper_submitted BOOLEAN DEFAULT FALSE,
  session_one BOOLEAN DEFAULT FALSE,
  session_two BOOLEAN DEFAULT FALSE,
  session_three BOOLEAN DEFAULT FALSE,
  session_four BOOLEAN DEFAULT FALSE,
  session_five BOOLEAN DEFAULT FALSE
);

-- Awards

CREATE TYPE award_type AS ENUM ('BEST_DELEGATE', 'OUTSTANDING_DELEGATE', 'HONORABLE_DELEGATE');

CREATE TABLE award (
  id SERIAL PRIMARY KEY,
  position_id INTEGER REFERENCES position,
  committee_id INTEGER NOT NULL REFERENCES committee,
  type award_type NOT NULL
);

-- For printing awards
CREATE VIEW award_print AS
  SELECT
    a.id AS id, a.type AS type,
    c.name AS committee_name,
    p.name AS position_name,
    s.name AS school_name,
    d.name AS delegate_name
  FROM award a
  INNER JOIN committee c on a.committee_id = c.id
  INNER JOIN position p on a.position_id = p.id
  INNER JOIN delegate d on d.position_id = p.id
  INNER JOIN school s ON d.school_id = s.id;

---  Print Requests

CREATE EXTENSION IF NOT EXISTS lo;

CREATE TABLE print_request (
  id SERIAL PRIMARY KEY,
  num_copies INTEGER NOT NULL DEFAULT 1,
  delivery_location TEXT NOT NULL,
  submission_time TIMESTAMP WITH TIME ZONE,
  requester TEXT NOT NULL,
  filename TEXT NOT NULL,
  content_type TEXT,
  data lo NOT NULL
);
