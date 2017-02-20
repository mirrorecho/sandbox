from abjad import *
from my_rhythm_maker import *
from my_pitches import *

durations = [Duration(1,2)] * 8
rhythms = my_rhythm_maker(durations)
staff = Staff(rhythms)
for i, leaf in enumerate(staff[:]):
	leaf.written_pitch = pitch_tuple[i]


# for i, leaf in enumerate(staff[:]):
# 	index = i % len(my_pitches)
# 	pitch = my_pitches[index]
# 	leaf.written_pitch = pitch