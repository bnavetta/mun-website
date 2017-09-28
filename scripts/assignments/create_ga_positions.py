import csv
import os

import psycopg2
from psycopg2.extras import execute_values

INSERT_POSITION = 'INSERT INTO position (name, committee_id) VALUES %s'

def create_ga_positions():
    with psycopg2.connect(host='localhost', dbname='busun', user='busun', password=os.environ['DB_PASSWORD']) as conn:
        with open('ga_positions.csv') as f:
            r = csv.reader(f)
            next(r) # skip header

            values = [(row[2], row[0]) for row in r]
        
        with conn.cursor() as cur:
            execute_values(cur, INSERT_POSITION, values)
        

if __name__ == '__main__':
    create_ga_positions()