-- Create table for storing the print queue

CREATE EXTENSION IF NOT EXISTS lo;

CREATE TABLE print_request (
  id SERIAL PRIMARY KEY,
  num_copies INT,
  delivery_location TEXT NOT NULL,
  submission_time TIMESTAMP,
  requester TEXT NOT NULL,
  filename TEXT NOT NULL,
  content_type TEXT NOT NULL,
  status TEXT NOT NULL,
  data lo NOT NULL
);

CREATE TRIGGER print_request_data BEFORE UPDATE OR DELETE ON print_request
  FOR EACH ROW EXECUTE PROCEDURE lo_manage(data);