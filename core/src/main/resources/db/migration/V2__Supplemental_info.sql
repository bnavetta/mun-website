-- Add the supplemental info form

CREATE TYPE luggage_storage AS ENUM ('FRIDAY', 'SUNDAY', 'BOTH', 'NONE');

CREATE TABLE supplemental_info (
  id INTEGER REFERENCES school PRIMARY KEY,

  -- Basic info
  phone_number TEXT,

  -- Address
  street_address TEXT,
  city TEXT,
  state TEXT,
  postal_code TEXT,
  country TEXT,

  -- Financial aid
  financial_aid BOOLEAN DEFAULT FALSE,
  financial_aid_amount DOUBLE PRECISION DEFAULT 0,
  financial_aid_documentation TEXT,

  -- Hotel info
  busun_hotel_id INTEGER REFERENCES hotel,
  non_busun_hotel TEXT,

  -- Shuttle info
  shuttle_friday_afternoon BOOLEAN DEFAULT FALSE,
  shuttle_friday_night BOOLEAN DEFAULT FALSE,
  shuttle_saturday BOOLEAN DEFAULT FALSE,
  shuttle_sunday BOOLEAN DEFAULT FALSE,

  -- Commuting
  commuting BOOLEAN DEFAULT FALSE,
  cars_parking INTEGER DEFAULT 0,
  car_parking_days INTEGER DEFAULT 0,
  bus_parking INTEGER DEFAULT 0,
  bus_parking_days INTEGER DEFAULT 0,

  -- Logistics
  arrival_time TIME WITHOUT TIME ZONE,
  luggage_storage luggage_storage,
  delegate_social_need_advisor BOOLEAN DEFAULT TRUE,
  delegate_count INTEGER DEFAULT 0,
  chaperone_count INTEGER DEFAULT 0,
  chaperone_info TEXT,
  parli_pro_training_count INTEGER DEFAULT 0,
  crisis_training_count INTEGER DEFAULT 0,
  tour_count INTEGER DEFAULT 0,
  info_session_count INTEGER DEFAULT 0
);