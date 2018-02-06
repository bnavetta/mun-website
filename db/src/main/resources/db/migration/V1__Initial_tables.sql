--- School tables

CREATE TABLE school (
  id SERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  has_applied BOOLEAN DEFAULT FALSE,
  accepted BOOLEAN DEFAULT FALSE,
  registration_code TEXT NOT NULL
);

CREATE TABLE supplemental_info (
  id INTEGER PRIMARY KEY REFERENCES school
);

CREATE TABLE school_application (
  id INTEGER PRIMARY KEY REFERENCES school
);

--- Advisor table

CREATE TABLE advisor (
  id SERIAL PRIMARY KEY,
  name TEXT,
  email TEXT UNIQUE NOT NULL,
  phone_number TEXT,
  password TEXT,
  school_id INTEGER REFERENCES school
);

--- Committee tables

CREATE TYPE committee_type AS ENUM ('GENERAL', 'SPECIALIZED', 'CRISIS');

CREATE TABLE committee (
  id SERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  description TEXT,
  short_name TEXT UNIQUE NOT NULL,
  type committee_type NOT NULL,
  topic1 TEXT,
  topic2 TEXT,
  topic3 TEXT,
  topic4 TEXT
);

CREATE TABLE joint_crisis (
  id SERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  short_name TEXT UNIQUE NOT NULL,
  description TEXT
);

CREATE TABLE joint_crisis_committees (
  joint_crisis_id INTEGER REFERENCES joint_crisis,
  committee_id INTEGER REFERENCES committee,
  PRIMARY KEY (joint_crisis_id, committee_id)
);

--- Position table

CREATE TABLE position (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  committee_ID INTEGER NOT NULL REFERENCES committee,
  UNIQUE (committee_id, name)
);

--- Delegate table

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

--- Award table

CREATE TYPE award_type AS ENUM ('BEST_DELEGATE', 'OUTSTANDING_DELEGATE', 'HONORABLE_DELEGATE');

CREATE TABLE award (
  id SERIAL PRIMARY KEY,
  position_id INTEGER REFERENCES position,
  committee_id INTEGER NOT NULL REFERENCES committee,
  type award_type NOT NULL
);

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