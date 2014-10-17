from abjad import *

score = Score([])

# objects to represent important musical ideas

# what can the computer do that's BETTER...?
# - quick experimentation
# - intricate textures
# - iterate accross an entire work
# - non-linear musical thinking!!!!!!!!

# TRY TO THINK MUSICALLY THROUGH THE CODE AND MOVEMENT STRUCTURES


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


