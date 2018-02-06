ALTER TABLE school_info
    ALTER COLUMN using_shuttle SET DEFAULT FALSE,
    ALTER COLUMN commuting SET DEFAULT FALSE,
    ALTER COLUMN car_count SET DEFAULT 0,
    ALTER COLUMN car_days SET DEFAULT 0,
    ALTER COLUMN bus_count SET DEFAULT 0,
    ALTER COLUMN bus_days SET DEFAULT 0,
    ALTER COLUMN shuttle_friday_afternoon SET DEFAULT FALSE,
    ALTER COLUMN shuttle_friday_night SET DEFAULT FALSE,
    ALTER COLUMN shuttle_saturday SET DEFAULT FALSE,
    ALTER COLUMN shuttle_sunday SET DEFAULT FALSE,
    ALTER COLUMN luggage_storage SET DEFAULT 'NONE',
    ALTER COLUMN band_color SET DEFAULT 'NOT_ATTENDING',
    ALTER COLUMN requested_delegates SET DEFAULT 1,
    ALTER COLUMN requested_chaperones SET DEFAULT 1,
    ALTER COLUMN parli_training_count SET DEFAULT 0,
    ALTER COLUMN crisis_training_count SET DEFAULT 0,
    ALTER COLUMN tour_count SET DEFAULT 0,
    ALTER COLUMN info_session_count SET DEFAULT 0;

UPDATE school_info SET using_shuttle = FALSE WHERE using_shuttle IS NULL;
UPDATE school_info SET commuting = FALSE WHERE commuting IS NULL;
UPDATE school_info SET car_count = 0 WHERE car_count IS NULL;
UPDATE school_info SET car_days = 0 WHERE car_days IS NULL;
UPDATE school_info SET bus_count = 0 WHERE bus_days IS NULL;
UPDATE school_info SET bus_days = 0 WHERE bus_days IS NULL;
UPDATE school_info SET shuttle_friday_afternoon = FALSE WHERE shuttle_friday_afternoon IS NULL;
UPDATE school_info SET shuttle_friday_night = FALSE WHERE shuttle_friday_night IS NULL;
UPDATE school_info SET shuttle_saturday = FALSE WHERE shuttle_saturday IS NULL;
UPDATE school_info SET shuttle_sunday = FALSE WHERE shuttle_sunday IS NULL;
UPDATE school_info SET luggage_storage = 'NONE' WHERE luggage_storage IS NULL;
UPDATE school_info SET band_color = 'NOT_ATTENDING' WHERE band_color IS NULL;
UPDATE school_info SET requested_delegates = 1 WHERE requested_delegates IS NULL;
UPDATE school_info SET requested_chaperones = 1 WHERE requested_chaperones IS NULL;
UPDATE school_info SET parli_training_count = 0 WHERE parli_training_count IS NULL;
UPDATE school_info SET crisis_training_count = 0 WHERE crisis_training_count IS NULL;
UPDATE school_info SET tour_count = 0 WHERE tour_count IS NULL;
UPDATE school_info SET info_session_count = 0 WHERE info_session_count IS NULL;