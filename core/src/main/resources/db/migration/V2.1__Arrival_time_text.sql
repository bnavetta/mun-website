-- Change the arrival_time column to text so we don't have to try to parse and validate advisor form inputs

ALTER TABLE supplemental_info ALTER COLUMN arrival_time SET DATA TYPE text;
