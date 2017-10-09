#!/usr/bin/env python

import csv
import os
import psycopg2


def create_position(cur, committee_id, position_name):
    """Create a position on the given committee and return the position id."""
    cur.execute("INSERT INTO position (name, committee_id) VALUES (%s, %s) RETURNING id;", (position_name, committee_id))
    return cur.fetchone()[0]


def assign_position(cur, position_id, school_id):
    """
    Create a delegate for the given school and assign them the given position.
    """
    cur.execute("INSERT INTO delegate (school_id) VALUES (%s) RETURNING id;", (school_id,))
    delegate_id = cur.fetchone()[0]
    cur.execute("UPDATE position SET delegate_id = %s WHERE id = %s;", (delegate_id, position_id))


def load_assignments():
    with psycopg2.connect(host='localhost', dbname='busun', user='busun', password=os.environ['DB_PASSWORD']) as conn:
        with conn.cursor() as cur:
            with open('assignments_final.csv') as f:
                r = csv.reader(f)
                next(r)
                for row in r:
                    school_id = int(row[1])
                    committee_id = int(row[3])
                    position_name = row[5]

                    position_id = create_position(cur, committee_id, position_name)
                    assign_position(cur, position_id, school_id)

if __name__ == '__main__':
    load_assignments()