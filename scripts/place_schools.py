#!/usr/bin/env python

import csv
import random
import sys

ROOMS = [
    ('Salomon Main Floor', 449 - 10),
    ('Salomon Balcony', 145),
    ('Salomon 001', 224),
    ('85 Waterman', 200)
]


def read_schools(filename):
    with open(filename) as f:
        out = []
        for row in csv.DictReader(f):
            chaperones = int(row['Chaperones'])
            delegates = int(row['Delegates'])
            name = row['School']
            out.append(dict(name=name, count=chaperones+delegates))
    return out


def fill_room(room, available, schools, writer):
    while len(schools) > 0:
        school = schools[0]
        if school['count'] <= available:
            print("{} in {}".format(school['name'], room))
            writer.writerow({ 'Room': room, 'School': school['name'], 'Delegation Size': school['count'] })
            available -= school['count']
            schools.pop(0)
        else:
            print("===> {} seats unfilled in {}".format(available, room))
            return

def position_schools(schools, output):
    random.shuffle(schools)
    with open(output, 'w') as f:
        writer = csv.DictWriter(f, fieldnames=['Room', 'School', 'Delegation Size'])
        writer.writeheader()
        for (room, available) in ROOMS:
            fill_room(room, available, schools, writer)


if __name__ == '__main__':
    schools = read_schools(sys.argv[1])
    position_schools(schools, sys.argv[2])
