ALTER TABLE school_info
    ADD COLUMN applying_for_aid BOOLEAN DEFAULT FALSE,
    ADD COLUMN aid_amount DECIMAL DEFAULT 0,
    ADD COLUMN aid_documentation TEXT