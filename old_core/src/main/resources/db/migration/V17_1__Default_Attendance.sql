ALTER TABLE position ALTER COLUMN attendance_record SET DEFAULT E'\\000';
UPDATE position SET attendance_record = E'\\000' WHERE attendance_record IS NULL;