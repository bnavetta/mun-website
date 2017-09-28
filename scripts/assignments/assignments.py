#!/usr/bin/env python3

import random
import csv
import requests

# load schools from google form csv
# for countries, do country -> list of committees
# for spec/crisis, have newline-delimited file

base = 'http://localhost:8080/adminp/assignment-params/'

# countries_json = requests.get(base + 'ga-countries').json()
# countries = [pair for pair in countries_json.iteritems()]
# general_position_count = sum(len(posns) for (_, posns) in countries)

# spec_positions = requests.get(base + 'spec-positions').json()
# spec_position_count = len(spec_positions)

# crisis_positions = requests.get(base + 'crisis-positions').json()
# crisis_position_count = len(crisis_positions)

general_position_count = 50
spec_position_count = 20
crisis_position_count = 30

countries = [
    ('France', [1, 2, 3, 4, 5, 6, 7, 8]), # (name, position IDs)
    ('United States', [9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]),
    ('Israel', [21, 22, 23, 24, 25, 26]),
    ('New York', [27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40]),
    ('Russia', [41, 42, 43, 44, 45, 46, 47, 48, 49, 50])
]

spec_positions = [
    'The Pope',
    'Italy or something',
    'Parliament',
    'Other Parliament',
    'Adam',
    'Adam 2',
    'Adam 3',
    'Adam 4',
    'Jordan',
    'Jourdan',
    'Cathy',
    'Bia',
    'Tess',
    'Colette',
    'Bethany',
    'Ben',
    'Alex',
    'ALEX',
    'aLeX',
    'ALEXXXXXXXX'
]

crisis_positions = []

class School(object):
    def __init__(self, name, general_pref, spec_pref, crisis_pref):
        self.name = name
        # Requested # of delegates of each type
        self.general_pref = general_pref
        self.spec_pref = spec_pref
        self.crisis_pref = crisis_pref

        # TODO: assert ^^ sums up to delegation size

        # Granted # of delegates of each type
        self.general_assigned = 0
        self.spec_assigned = 0
        self.crisis_assigned = 0

        self.positions = []
    
    def slots_remaining(self):
        return self.general_pref + self.spec_pref + self.crisis_pref

schools = [
    School("Groton-Dunstable", 10, 5, 6),
    School("Stuy", 6, 4, 15),
    School("PASA", 20, 0, 0),
    School("Wheeler", 15, 8, 8)
]

while True:
    progress = False # track when we stop assigning
    random.shuffle(schools)
    for school in schools:
        if school.slots_remaining() == 0:
            continue
        # TODO: make this part more random
        if school.general_pref > 0 and general_position_count > 0:
            school.general_assigned = school.general_assigned + 1
            school.general_pref = school.general_pref - 1
            general_position_count = general_position_count - 1
        elif school.spec_pref > 0 and spec_position_count > 0:
            school.spec_assigned = school.spec_assigned + 1
            school.spec_pref = school.spec_pref - 1
            spec_position_count = spec_position_count - 1
        elif school.crisis_pref > 0 and crisis_position_count > 0:
            school.crisis_assigned = school.crisis_assigned + 1
            school.crisis_pref = school.crisis_pref - 1
            crisis_position_count = crisis_position_count - 1
        else:
            if general_position_count > spec_position_count and general_position_count > crisis_position_count:
                school.general_assigned = school.general_assigned + 1
                school.general_pref = school.general_pref - 1
                general_position_count = general_position_count - 1
            elif spec_position_count > general_position_count and spec_position_count > crisis_position_count:
                school.spec_assigned = school.spec_assigned + 1
                school.spec_pref = school.spec_pref - 1
                spec_position_count = spec_position_count - 1
            elif crisis_position_count > 0:
                school.crisis_assigned = school.crisis_assigned + 1
                school.crisis_pref = school.crisis_pref - 1
                crisis_position_count = crisis_position_count - 1
            else:
                raise Exception("Insufficient remaining positions")
        progress = True
    if not progress:
        break

print("Committee type breakdown:")

for school in schools:
    print("{0.name} got {0.general_assigned} general, {0.spec_assigned} spec, and {0.crisis_assigned} crisis positions".format(school))

# GA assignments
while True:
    random.shuffle(schools)
    open_slots = 0
    for school in schools:
        open_slots += school.general_assigned
        if school.general_assigned > 0:
            country_name, country_positions = random.choice(countries)
            posn_count = min(len(country_positions), school.general_assigned, 2)
            if posn_count > 0:
                posns = [country_positions.pop() for _ in range(posn_count)]
                school.positions.extend(posns)
                school.general_assigned -= posn_count
    if open_slots <= 0:
        break

print("\nUnassigned GA positions:")

for country in countries:
    print("{0} had these positions unassigned: {1}".format(*country))

# Spec assignments
while len(spec_positions) > 0:
    random.shuffle(schools)
    open_slots = 0
    for school in schools:
        open_slots += school.spec_assigned
        if school.spec_assigned > 0:
            i = random.randrange(len(spec_positions))
            posn = spec_positions.pop(i)
            school.positions.append(posn)
            school.spec_assigned -= 1
    if open_slots <= 0:
        break

# Crisis assignments
while len(crisis_positions) > 0:
    random.shuffle(schools)
    open_slots = 0
    for school in schools:
        open_slots += school.crisis_assigned
        if school.crisis_assigned > 0:
            i = random.randrange(len(crisis_positions))
            posn = crisis_positions.pop(i)
            school.positions.append(posn)
            school.crisis_assigned -= 1
    if open_slots <= 0:
        break

assert(len(crisis_positions) == 0)

print("\nSchool position assignments:")

for school in schools:
    print("{0.name} was assigned positions {0.positions}".format(school))

with open('assignments.csv', 'w') as f:
    w = csv.writer(f)
    for school in schools:
        row = [school.name]
        row.extend(school.positions)
        w.writerow(row)
