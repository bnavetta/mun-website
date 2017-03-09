CREATE TABLE attendance(
    delegate_id BIGINT PRIMARY KEY REFERENCES delegate(id),
    position_paper BOOLEAN,

    session_one_present BOOLEAN,
    session_one_tally INTEGER,

    session_two_present BOOLEAN,
    session_two_tally INTEGER,

    session_three_present BOOLEAN,
    session_three_tally INTEGER,

    session_four_present BOOLEAN,
    session_four_tally INTEGER,

    session_five_present BOOLEAN,
    session_five_tally INTEGER
);