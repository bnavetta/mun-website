import csv
import json
import os
import secrets

import bcrypt
import psycopg2
import requests

class PasswordGenerator(object):
    def __init__(self):
        with open('words') as f:
            self.words = [word.strip() for word in f]

    def generate(self):
        return ' '.join(secrets.choice(self.words) for i in range(6))

pw_generator = PasswordGenerator()

with open('acceptance_email.html') as f:
    acceptance_email = f.read()

with open('acceptance_email.txt') as f:
    acceptance_text = f.read()

def read_advisors(filename):
    with open(filename, newline='') as csv_file:
        reader = csv.reader(csv_file)
        for row in reader:
            yield dict(school_name=row[0], advisor_name=row[1], advisor_email=row[2])

def insert_school(cur, advisor):
    cur.execute('INSERT INTO school (name) VALUES (%s) RETURNING id;', (advisor['school_name'],))
    return cur.fetchone()[0]

def insert_advisor(cur, advisor, school_id):
    password = pw_generator.generate()
    password_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt(10, prefix=b'2a')).decode('utf-8')
    cur.execute("INSERT INTO advisor (name, email, is_primary, phone_number, password, school_id) VALUES (%s, %s, true, '', %s, %s)",
        (advisor['advisor_name'], advisor['advisor_email'], password_hash, school_id)
    )
    return password

def send_emails(api_key, email_vars):
    recipients = [entry['advisor_email'] for entry in email_vars]
    params = {
        'from': 'admin@busun.org',
        'to': recipients,
        'subject': 'BUSUN XXI Application',
        'html': acceptance_email,
        'text': acceptance_text,
        'recipient-variables': json.dumps({ entry['advisor_email']: entry for entry in email_vars }),
        'h:Reply-To': 'technology@busun.org'
    }
    resp = requests.post('https://api.mailgun.net/v3/mg.busun.org/messages', auth=('api', api_key), data=params)
    print(resp.text)
    resp.raise_for_status()

if __name__ == '__main__':
    email_vars = []
    with psycopg2.connect(dbname='busun', user='busun', password=os.environ['DB_PASSWORD']) as conn:
        with conn.cursor() as cursor:
            for advisor in read_advisors('accepted.csv'):
                school_id = insert_school(cursor, advisor)
                print('Created school "{}" with ID {}'.format(advisor['school_name'], school_id))
                password = insert_advisor(cursor, advisor, school_id)
                print('Created advisor {} with password "{}" for {}'.format(advisor['advisor_email'], password, school_id))
                params = dict(advisor)
                params['password'] = password
                email_vars.append(params)
    print('Emailing advisors...')
    send_emails(os.environ['MAILGUN_API_KEY'], email_vars)
    print('Done!')
