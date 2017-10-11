ALTER TABLE joint_crisis ADD COLUMN short_name TEXT UNIQUE;

ALTER TABLE committee ADD CONSTRAINT unique_short_name UNIQUE(short_name);
