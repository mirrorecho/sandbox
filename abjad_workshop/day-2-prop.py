# -*- coding:utf-8 -*-
from abjad import *

proportions = [(1,n) for n in range(11+1)]

def make_nested_tuplet(
        tuplet_duration,
        outer_tuplet_proportions,
        inner_tuplet_subdivision_count,
        ):
    outer_tuplet = Tuplet.from_duration_and_ratio(tuplet_duration,
            outer_tuplet_proportions)
    inner_tuplet_proportions = inner_tuplet_subdivision_count * [1]
    # last_leaf = next(iterate(oter_tuplet).by_leaf())
    last_leaf = outer_tuplet[-1]
    right_logical_tie = inspect_(last_leaf).get_logical_tie() # e.g. handles case of 5 eight notes!
    right_logical_tie.to_tuplet(inner_tuplet_proportions)
    return outer_tuplet

# tuplet = make_nested_tuplet(Duration(1,4), (1,1), 5)
# f(tuplet)
# show(tuplet)

# tuplets = []
# tuplets.append(make_nested_tuplet(Duration(1,4), (5,1), 5))

# def show_tuplet(index):
#     staff = Staff([tuplets[index]], context_name="RhythmicStaff")
#     show(staff)

def make_row_of_nested_tuplets(
        tuplet_duration,
        outer_tuplet_proportions,
        column_count
        ):
    assert 0 < column_count
    row_of_nested_tuplets = []
    for n in range(column_count):
        inner_tuplet_subdivision_count = n + 1
        nested_tuplet = make_nested_tuplet(
                tuplet_duration,
                outer_tuplet_proportions,
                inner_tuplet_subdivision_count
                )
        row_of_nested_tuplets.append(nested_tuplet)
    return row_of_nested_tuplets

tuplets = make_row_of_nested_tuplets(Duration(1,2), (1,1), 12)
staff = Staff(tuplets, context_name="RhythmicStaff")
show(staff)


