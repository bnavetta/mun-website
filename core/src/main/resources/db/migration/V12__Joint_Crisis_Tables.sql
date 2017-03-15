--- JointCrisis table

CREATE TABLE joint_crisis (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT
);

CREATE TABLE joint_crisis_committees (
    crisis_id INTEGER REFERENCES joint_crisis(id),
    committee_id INTEGER REFERENCES committee(id)
);
