from abjad import *

score = Score([])

# objects to represent important musical ideas

# what can the computer do that's BETTER...?
# - quick experimentation
# - intricate textures
# - iterate accross an entire work
# - non-linear musical thinking!!!!!!!!

# TRY TO THINK MUSICALLY THROUGH THE CODE AND MOVEMENT STRUCTURES

# class represents a musical idea...
# - a form of "short score"?

# TECHNICAL STUFF:
# - easily add & remove staves
# - easily create non-metered music

# HARMONIC PALETTE...
# 2 or 3 random walks through cicle of 5ths (perlin noise or regular noise?) + harmonics above that.
# (show these all in a row)

# SOLO FLUTE PIECE



# cloudify (KEEP)

note = Note("c'4")

note.written_duration = Duration(2, 4)

note2 = Note("a'8")



def make_staff(inst_name, clef_name):
    # make bass voice and staff
    this_voice = scoretools.Voice(inst_name=name + ' Voice')
    this_staff = scoretools.Staff([this_voice], inst_name=name + ' Staff')
    clef = indicatortools.Clef(clef_name)
    attach(clef, this_staff)
    # contrabass = instrumenttools.Contrabass(
    #     short_instrument_name_markup='Cb.'
    #     )
    #attach(contrabass, bass_staff)
    return this_staff


flute_staff=make_staff('Flute', 'treble')


flute_staff.append(note)
flute_staff.append(note2)

show(score)


