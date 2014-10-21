from abjad import *

score = Score([])

# objects to represent important musical ideas

# what can the computer do that's BETTER...?
# - quick experimentation
# - intricate textures
# - iterate accross an entire work
# - non-linear musical thinking!!!!!!!!

# TRY TO THINK MUSICALLY THROUGH THE CODE AND MOVEMENT STRUCTURES

# A MUTABLE "GHOST THING" ....
# NEED TO FIGURE OUT POSSIBLE MUTATIONS




note = Note("c'4")

note.written_duration = Duration(2, 4)

note2 = Note("a'8")



def make_staff(inst_name, clef_name):
    # make voice and staff
    this_voice = scoretools.Voice(name=inst_name + ' Voice') # is the voice needed????
    this_staff = scoretools.Staff([this_voice], name=inst_name + ' Staff')
    clef = indicatortools.Clef(clef_name)
    attach(clef, this_staff)
    # contrabass = instrumenttools.Contrabass(
    #     short_instrument_name_markup='Cb.'
    #     )
    #attach(contrabass, bass_staff)
    return this_staff


flute_staff=make_staff('Flute', 'treble')
flute_staff2=make_staff('Flute 2', 'treble')

flute_staff.extend("c1 d2 g2 e32 e e")
flute_staff2.extend(Rest(flute_staff))


#flute_staff.append(note)
#flute_staff.append(note2)

score.extend([flute_staff, flute_staff2])

show(score)


