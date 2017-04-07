ALTER TABLE school
    DROP COLUMN number_of_years_attended,
    DROP COLUMN years_attended,
    DROP COLUMN about_text;

CREATE TABLE school_info (
    id INTEGER PRIMARY KEY REFERENCES school(id),
    country TEXT,                           -- Address fields
    street_address TEXT,
    city TEXT,
    state TEXT,
    postal_code TEXT,
    phone_number TEXT,
    hotel_id INTEGER REFERENCES hotel (id),
    other_hotel TEXT,
    using_shuttle BOOLEAN,
    commuting BOOLEAN,
    car_count INTEGER,
    car_days INTEGER,
    bus_count INTEGER,
    bus_days INTEGER,
    shuttle_friday_afternoon BOOLEAN,
    shuttle_friday_night BOOLEAN,
    shuttle_saturday BOOLEAN,
    shuttle_sunday BOOLEAN,
    arrival_time TIMESTAMP,
    luggage_storage TEXT,
    band_color TEXT,
    requested_delegates INTEGER,
    requested_chaperones INTEGER,
    parli_training_count INTEGER,
    crisis_training_count INTEGER,
    tour_count INTEGER,
    info_session_count INTEGER,
    chaperone_info TEXT
);

INSERT INTO school_info (id, country, street_address, city, state,
                         postal_code, phone_number, hotel_id, using_shuttle,
                         commuting, requested_delegates, requested_chaperones)
    SELECT
        id, country, street_address, city, state,
        postal_code, phone_number, hotel_id, using_shuttle,
        commuting, requested_delegates, requested_chaperones
    FROM school;

ALTER TABLE school
    DROP COLUMN country,
    DROP COLUMN street_address,
    DROP COLUMN city,
    DROP COLUMN state,
    DROP COLUMN postal_code,
    DROP COLUMN phone_number,
    DROP COLUMN hotel_id,
    DROP COLUMN using_shuttle,
    DROP COLUMN commuting,
    DROP COLUMN requested_chaperones,
    DROP COLUMN requested_delegates;