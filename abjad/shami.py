from abjad import *
import copy

score = Score([])

# objects to represent important musical ideas

# what can the computer do that's BETTER...?
# - quick experimentation
# - intricate textures
# - iterate accross an entire work
# - non-linear musical thinking!!!!!!!!

# TRY TO THINK MUSICALLY THROUGH THE CODE AND MOVEMENT STRUCTURES

staff1 = scoretools.Staff(context_name='RhythmicStaff')
staff2 = scoretools.Staff(context_name='RhythmicStaff')


note = Note("c'4")
note.written_duration = Duration(2, 4)

note2 = Note("c'8")
note2.written_duration = Duration(1,4)

staff1.append(note)
staff1.append(copy.deepcopy(note))
staff2.append(note2)

score.append(staff1);
score.append(staff2);

show(score)
