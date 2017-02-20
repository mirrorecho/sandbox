
from abjad import *


def make_rhythm1(duration_fractions):
	tie_specifier = rhythmmakertools.TieSpecifier(tie_across_divisions=[True, False, False, True])
	rhythm_maker = rhythmmakertools.NoteRhythmMaker(tie_specifier=tie_specifier)
	rhythm = rhythm_maker([Duration(d) for d in duration_fractions])
	staff = Staff(rhythm)
	return staff

def make_rhythm2(
			duration_fractions=((1,2),(1,2)), 
			talea_counts=(1,2,3,4), 
			talea_denominator=8, 
			time_signature_fraction=(4,4),
			extra_counts_per_division=None,
			sustain_mask_period = None,
			sustain_mask_indices = (0,),
			):
	talea = rhythmmakertools.Talea(talea_counts, talea_denominator)
	if sustain_mask_period:
		division_masks = [
					rhythmmakertools.SustainMask(
						patterntools.Pattern(sustain_mask_indices, period=sustain_mask_period)
						),
				]
	else:
		division_masks = None
	rhythm_maker = rhythmmakertools.TaleaRhythmMaker(
			division_masks=division_masks,
			talea=talea, 
			extra_counts_per_division=extra_counts_per_division)
	rhythm = rhythm_maker([Duration(d) for d in duration_fractions])
	staff = Staff(rhythm)
	time_signature = TimeSignature(time_signature_fraction)
	attach(time_signature, staff)
	return staff

# durations = [Duration(3,4), Duration(5,8), Duration(3,16), Duration(1,4), Duration(4,4), Duration(5,8) ]

# rhythm1_maker = rhythmmakertools.NoteRhythmMaker()

# rhythm1 = rhythm1_maker(durations)
# staff = Staff([rhythm1])

# show(rhythm1)