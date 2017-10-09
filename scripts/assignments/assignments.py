#!/usr/bin/env python3

import random
import csv

general_position_count = 0

with open('ga_positions2.csv') as f:
    ga_positions = dict()
    r = csv.reader(f)
    for row in r:
        committee = row[0]
        country = row[1]
        general_position_count += 1
        posn = committee + ':' + country
        if country in ga_positions:
            ga_positions[country].append(posn)
        else:
            ga_positions[country] = [posn]

    countries = list(ga_positions.items())

with open('spec.txt') as f:
    spec_positions = [p.strip() for p in f]
    spec_position_count = len(spec_positions)

with open('crisis.txt') as f:
    crisis_positions = [p.strip() for p in f]
    crisis_position_count = len(crisis_positions)

class School(object):
    def __init__(self, id, name, general_pref, spec_pref, crisis_pref):
        self.id = id
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

schools = []
with open('schools.csv') as f:
    r = csv.reader(f)
    next(r)
    for row in r:
        schools.append(School(row[0], row[1], int(row[2]), int(row[3]), int(row[4])))

delegate_count = 0
for school in schools:
    delegate_count += school.general_pref + school.spec_pref + school.crisis_pref

print("{} delegates and {} positions".format(delegate_count, general_position_count + spec_position_count + crisis_position_count))

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
    w.writerow(['school_name', 'position_id', 'position_name'])
    for school in schools:
        for posn in school.positions:
            w.writerow([school.name, posn])
