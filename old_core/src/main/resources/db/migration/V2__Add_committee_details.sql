ALTER TABLE committee ADD COLUMN short_name TEXT;
ALTER TABLE committee ADD COLUMN joint_crisis BOOLEAN;
ALTER TABLE committee ADD COLUMN topic1 TEXT;
ALTER TABLE committee ADD COLUMN topic2 TEXT;
ALTER TABLE committee ADD COLUMN topic3 TEXT;
ALTER TABLE committee ADD COLUMN topic4 TEXT;

UPDATE committee SET joint_crisis = FALSE WHERE joint_crisis = NULL;