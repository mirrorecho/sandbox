\version "2.19.82"
\language "english"

\header {
    tagline = ##f
}

\layout {}

\paper {}

\score {
    \new Score
    <<
        \context StaffGroup = "piano_string0"
        <<
            \context Staff = "pluck_staff"
            \with
            {
                \consists Horizontal_bracket_engraver
            }
            {
                {
                    \override Staff.Glissando.springs-and-rods = #ly:spanner::set-spacing-rods
                                \override Staff.Glissando.style = #'dashed-line
                                \override Staff.Glissando.minimum-length = #3.2
                                \override Staff.NoteHead.no-ledgers = ##t
                                \override Glissando.bound-details.left.padding = #0.2
                                \override Glissando.bound-details.right.padding = #0.2
                                \override Glissando.bound-details.left.attach-dir = #0
                                \override Glissando.bound-details.right.attach-dir = #0.5
                                \set glissandoMap = #'(( 0 . 0) ( 0 . 3) ( 1 . 1) ( 1 . 2))
                    \accidentalStyle modern-cautionary
                    \clef "bass"
                    <a,, a,>1
                    \glissando
                    \stopStaff
                        \once \override Staff.Clef.transparent = ##t
                        \clef percussion
                        \stemUp
                        \hide Staff.StaffSymbol
                        
                    s1
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    <
                        c
                        \tweak transparent ##t
                        g
                        \tweak transparent ##t
                        c'
                        \tweak transparent ##t
                        f'
                    >8
                    [
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    <
                        \tweak transparent ##t
                        c
                        \tweak transparent ##t
                        a
                        \tweak transparent ##t
                        c'
                        a'
                    >8
                    ]
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    <
                        \tweak transparent ##t
                        c
                        \tweak transparent ##t
                        a
                        c'
                        \tweak transparent ##t
                        f'
                    >4
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    <
                        \tweak transparent ##t
                        c
                        a
                        \tweak transparent ##t
                        c'
                        \tweak transparent ##t
                        a'
                    >4
                    \glissando
                }
            }
            \context Staff = "reson_staff"
            \with
            {
                \consists Horizontal_bracket_engraver
            }
            {
            }
        >>
    >>
}