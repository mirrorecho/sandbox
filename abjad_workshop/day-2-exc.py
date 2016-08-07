from abjad import *

# note_1 = Note("c'4")
# note_2 = Note("d'4")
# note_3 = Note("e'4")
# note_4 = Note("f'4")
# notes = [ note_1, note_2, note_3, note_4 ]

voice_strs = (
	r""" g'4 | g'2 d''4 | b'4. a'8 g'4 | g'4. a'8 b'4 | 
	a'2\fermata b'4 | d''2 c''4 | b'4 a'2 | g'2\fermata """,
	""" d'4 | d'4 e'4 d'4 | d'2 b4 | e'8 d'8 e'8 fs'8 g'4 | fs'2 g'4 | d'4 e'4 fs'4 | g'2 fs'4 | d'2 """,
	r""" \clef bass
		b4 | b4 c'8 b8 a4 | g4 fs4 g4 | c'8 b8 c'4 d'4 | d'2 d'4 | a4 b4 c'4 | d'4 e'4 d'8 c'8 | b2 """,
	r""" \clef bass
		g,4 | g4 e4 fs4 | g4 d4 e4 | c4 b,8 a,8 g,4 | d2 g,4 | fs,4 g,4 a,4 | b,4 c4 d4 | g,2 """,
	)

voices = [Voice(v) for v in voice_strs]
attach(indicatortools.LilyPondCommand("voiceOne"), voices[0])
attach(indicatortools.LilyPondCommand("voiceTwo"), voices[1])
attach(indicatortools.LilyPondCommand("voiceThree"), voices[2])
attach(indicatortools.LilyPondCommand("voiceFour"), voices[3])

for voice in voices:
	attach(indicatortools.BarLine(abbreviation="|."), voice[-1])

staves = []
staves.append(
	Staff([voices[0],voices[1]], is_simultaneous=True) )
staves.append(
	Staff([voices[2],voices[3]], is_simultaneous=True) )

for staff in staves:
	attach(KeySignature("g", "major"), staff)
	attach(TimeSignature((3,4), partial=Duration(1,4)), staff)

staff_group = StaffGroup(staves, context_name="PianoStaff")
score = Score([staff_group])
# f(score)
# show(score)
# show(voice)

