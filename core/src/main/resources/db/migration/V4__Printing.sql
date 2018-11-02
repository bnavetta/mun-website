CREATE TYPE print_request_status AS ENUM ('PENDING', 'CLAIMED', 'COMPLETED');

ALTER TABLE print_request ADD COLUMN status print_request_status NOT NULL ;
