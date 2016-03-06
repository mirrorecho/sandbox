import settings
from abjad import *
from calliope.bubbles import *

class MainScore(BubbleScore):
    violin1 = BubbleStaff(instrument=instrumenttools.Violin(instrument_name="Violin 1", short_instrument_name="vln.1"))
    violin2 = BubbleStaff(instrument=instrumenttools.Violin(instrument_name="Violin 2", short_instrument_name="vln.2"))
    viola = BubbleStaff(instrument=instrumenttools.Viola(instrument_name="Viola", short_instrument_name="vla."), clef="alto")
    cello = BubbleStaff(instrument=instrumenttools.Cello(instrument_name="Cello", short_instrument_name="vc."), clef="bass") 
    sequence = ("violin1", "violin2", "viola", "cello")

resting_1 = BubbleMaterial("strings.resting_1")

class Shell(Bubble):
    violin1 = Placeholder() # why does it not work unless we inherit from placeholder?
    violin2 = Placeholder()
    viola = Placeholder()
    cello = Placeholder()

# MAYBE TO DO... add quick ability for rest/space - filled bubbles (with measure durations as parameter)

class Pattern1(Shell):
    violin1 = Line("r1\\fermata")
    violin2 = resting_1
    viola = BubbleMaterial("strings.walking_1")
    cello = Line('a1\\fermata ^"sul tasto"')

class Pattern2(Pattern1):
    violin2 = Transpose( Pattern1.viola, 2)


class FinalMusic(GridSequence, Shell):
    grid_sequence = (Pattern1, Pattern1, Pattern2)

# maybe something like this... 
# class FinalTest(Shell):
#     music = Pattern1() * 3 + Pattern2()

score = MainScore( FinalMusic() )
score.make_pdf()
print(score)