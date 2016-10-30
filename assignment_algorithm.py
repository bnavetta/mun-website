from collections import namedtuple, deque
from enum import Enum
from itertools import repeat
from random import shuffle

Position = namedtuple('Position', ['country', 'committee'])
Committee = namedtuple('Committee', ['name', 'type'])
School = namedtuple('School', ['name', 'prefs', 'assignments'])

class SchoolPrefs(object):
    __slots__ = ['countries', 'percent_spec', 'percent_crisis', 'percent_general', 'allotted_delegates']

    def __init__(self, countries, percent_spec, percent_crisis, percent_general, allotted_delegates):
        self.countries = countries
        self.percent_spec = percent_spec
        self.percent_crisis = percent_crisis
        self.percent_general = percent_general
        self.allotted_delegates = allotted_delegates

    def __repr__(self):
        return 'SchoolPrefs(countries={}, percent_spec={}, percent_crisis={}, percent_general={}, allotted_delegates={}, spec_delegates={}, crisis_delegates={}, general_delegates={})'.format(self.countries, self.percent_spec, self.percent_crisis, self.percent_general, self.allotted_delegates, self.spec_delegates, self.crisis_delegates, self.general_delegates)

    @property
    def spec_delegates(self):
        return int(self.percent_spec * self.allotted_delegates / 100.0)

    @property
    def crisis_delegates(self):
        return int(self.percent_crisis * self.allotted_delegates / 100.0)

    @property
    def general_delegates(self):
        return int(self.percent_general * self.allotted_delegates / 100.0)

class SchoolAssignments(object):
    __slots__ = ['positions', 'spec_delegates', 'crisis_delegates', 'general_delegates']

    def __init__(self):
        self.positions = []
        self.spec_delegates = 0
        self.crisis_delegates = 0
        self.general_delegates = 0

    def __repr__(self):
        return 'SchoolAssignments(positions={}, spec_delegates={}, crisis_delegates={}. general_delegates={})'.format(self.positions, self.spec_delegates, self.crisis_delegates, self.general_delegates)

class CommitteeType(Enum):
    spec = 'spec'
    crisis = 'crisis'
    general = 'general'

positions = [
    Position('France', 'Something Special')
]
committees = {
    'Something Special': Committee('Something Special', CommitteeType.spec)
}
schools = [
    School('Some School', SchoolPrefs([], 30, 20, 50, 10), SchoolAssignments())
] # sorted by priority

def assign_category_counts():
    """figure out how many of spec/crisis/general each school has"""
    available_spec = 0
    available_crisis = 0
    available_general = 0
    for position in positions:
        committee = committees[position.committee]
        if committee.type == CommitteeType.spec:
            available_spec += 1
        elif committee.type == CommitteeType.crisis:
            available_crisis += 1
        elif committee.type == CommitteeType.general:
            available_general += 1

    print('{} spec positions, {} crisis positions, and {} general positions'.format(available_spec, available_crisis, available_general))

    for school in schools:
        desired_spec = school.prefs.spec_delegates
        desired_crisis = school.prefs.crisis_delegates
        desired_general = school.prefs.general_delegates

        # TODO: which category should be filled first (I'm assuming spec/crisis first since they're smaller and it's easier to add general delegates?)
        # also assuming more positions than delegates
        allotted_spec = min(desired_spec, available_spec)
        allotted_crisis = min(desired_crisis, available_crisis)
        allotted_general = school.prefs.allotted_delegates - allotted_spec - allotted_crisis
        available_spec -= allotted_spec
        available_crisis -= allotted_crisis
        available_general -= allotted_general
        school.assignments.spec_delegates = allotted_spec
        school.assignments.crisis_delegates = allotted_crisis
        school.assignments.general_delegates = allotted_general

def assign_spec_positions():
    """randomly assign spec positions"""
    spec_positions = deque([pos for pos in positions if committees[pos.committee].type == CommitteeType.spec])
    shuffle(spec_positions)
    for school in schools:
        for _ in repeat(None, school.assignments.spec_delegates):
            school.assignments.positions.append(spec_positions.pop())

assign_category_counts()
assign_spec_positions()

print(schools)
