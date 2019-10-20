import math
import abjad
import calliope

class PianoString(calliope.StaffGroup):
    class PluckStaff(calliope.Staff): pass
        # clef = "percussion"
    class ResonStaff(calliope.Staff): pass

class MyScore(calliope.Score):
    class PianoString0(PianoString): pass

class StringDefEvent(calliope.Event): 
    # from lowest to highest, maps pitches in self.pitch to index of string to pluck
    
    string_map = {0: (0,)}
    init_beats = 4
    pluck_spacing = 4
    clef = "bass"
    _current_tensions = None

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.scale = calliope.Scale()
        self.pitch = [p for p in self.string_map]
        # self._current_tensions = [0 for i in range(len(self.string_map))]

    @property
    def string_count(self):
        return sum([len(strings) for pitches, strings in self.string_map.items()])

    def get_pluck_pitches(self, tensions=(0,)):
        # TO DO... this math is odd and nasty
        start_index = 1 - math.floor((self.string_count * self.pluck_spacing) / 2)
        return tuple([
            self.scale[start_index+(i*self.pluck_spacing)+(tensions[i % len(tensions)])] 
            for i in range(self.string_count)
            ])

    # def get_pluck_pitches(self):
    #     # TO DO... this math is a little odd and nasty
    #     start_index = 1 - math.floor((self.string_count * self.pluck_spacing) / 2)
    #     return [
    #         self.scale[start_index+(i*self.pluck_spacing)+self._current_tensions[i]] 
    #         for i in range(self.string_count)
    #         ]


    # def get_pluck_pitch(self, string, tension=None): 
    #     # TO DO... this math is odd and nasty

    #     start_index = 1 - math.floor((self.string_count * self.pluck_spacing) / 2)
    #     return self.scale[start_index + (string*self.pluck_spacing) + tension] 

    def get_reson_pitch(self, string):
        # TO DO: could by simpler with next()
        for pitch, strings in self.string_map.items():
            for s in strings:
                if string == s:
                    return pitch

    def set_tension(self, string, tension):
        self._current_tensions[string] = tension

    def get_tension(self, string):
        return self._current_tensions[string]

    def get_reson_event(self):
        my_event = calliope.Event(
            pitch = list(self.string_map.keys()),
            beats = self.init_beats
            )
        my_event.tag(self.clef)
        my_event.tag(
            r"""\override Staff.Glissando.springs-and-rods = #ly:spanner::set-spacing-rods
            \override Staff.Glissando.style = #'dashed-line
            \override Staff.Glissando.minimum-length = #3.2
            \override Staff.NoteHead.no-ledgers = ##t
            \override Glissando.bound-details.left.padding = #0.2
            \override Glissando.bound-details.right.padding = #0.2
            \override Glissando.bound-details.left.attach-dir = #0
            \override Glissando.bound-details.right.attach-dir = #0.5
            \set glissandoMap = #'(""" + " ".join(
                ["( %s . %s)" % (i, v) for i,s in enumerate(self.string_map.items()) 
                for v in self.string_map[s[0]]]
                ) + ")"
            )
        my_event.tag("!\\glissando")
        return my_event

class StringEvent(calliope.Factory, calliope.Event): 
    ## TO DO... implement tuple for sim. plucks

    pluck_strings = (0,)
    tensions = (0,) # generally between -4 and 4 but could be more/less if things are wild
    string_def_event = None

    # @property
    # def pluck_pitch(self):
    #     return self.string_def_event.get_pluck_pitch(self.tension)

    @property
    def reson_pitch(self):
        return self.string_def_event.get_reson_pitch(self.string)

    def get_pluck_event(self):
        
        my_event = calliope.Event(
            beats = self.beats,
            pitch = self.string_def_event.get_pluck_pitches(self.tensions),
            pluck_strings = self.pluck_strings
            )
        # TO DO: move gliss styling to the beginning only
        my_event.tag(
            r"""\set glissandoMap = #'(""" + " ".join(
                ["( %s . %s )" % (i,i) 
                for i in range(self.string_def_event.string_count)]
                ) + ")"
            )
        my_event.tag("!\\glissando")
        return my_event


class StringCell(calliope.Factory, calliope.Cell): 
    branch_type = StringEvent
    rhythm = (1,)
    pluck_strings = ((0,),)
    tensions = ((0,),)
    string_def_event = None # must be set when instantiated

    def get_branches_kwargs(self, *args, **kwargs):
        return [
            dict(
                beats=r, 
                pluck_strings=s, 
                tensions=t,
                string_def_event = self.string_def_event
                ) for r, s, t in zip(
                self.rhythm, self.pluck_strings, self.tensions)
        ]

    def get_pluck_cell(self):
        my_cell = calliope.Cell(
            *[e.get_pluck_event() for e in self]
            )
        return my_cell

class StringSegment(calliope.Segment): 
    string_def_event = None # must be set when instantiated

    def process_logical_tie(self, music, music_logical_tie, data_logical_tie, leaf_index):
        pluck_strings = getattr(data_logical_tie.parent, "pluck_strings", None)
        if pluck_strings:
            for i, note_head in enumerate(music[leaf_index].note_heads):
                if i not in pluck_strings:
                    abjad.tweak(note_head).transparent = True

    def get_pluck_section(self):
        my_cell = calliope.Cell(
            *[e.get_pluck_event() for e in self]
            )


sde1 = StringDefEvent(
    string_map = {
        -27: (0, 3), 
        -15: (1, 2)
        }
    )

c = StringCell(
    string_def_event = sde1,
    pluck_strings = ((0,),(3,),(2,),(1,),),
    rhythm = (0.5, 0.5, 1, 1),
    tensions = (
        (0,0,-1,-2),
        (0,1,-1,0),
        (0,1,-1,-2),
        (0,1,-1,0),
        ),
    )

class StartPluckingEvent(calliope.Event): 
    # init_skip = True # TO DO... why doesn't this work???
    init_beats = 4
    string_def_event = None # must be set when instantiated
    # init_tags = (".",) # TO DO... why doesn't this work???

START_PLUCKING = StartPluckingEvent(
    )
START_PLUCKING.skip = True

START_PLUCKING.tag(
    # \override Glissando.minimum-length = #3
    # \override Score.SpacingSpanner.spacing-increment = #2
    r"""\stopStaff
    \once \override Staff.Clef.transparent = ##t
    \clef percussion
    \stemUp
    \hide Staff.StaffSymbol
    """
    )

# print(c.events[2].get_string_pitch())
# print(c)

p = MyScore()
abjad.override(p.staves[0]).glissando.style = "dashed-line"
p.staves[0].append(
    StringSegment(
        c.string_def_event.get_reson_event(),
        START_PLUCKING(),
        c.get_pluck_cell()
        )
    )

p.staves[0].events[0].tag()
# p.staves[0].events[2].skip = True

calliope.illustrate(p)
