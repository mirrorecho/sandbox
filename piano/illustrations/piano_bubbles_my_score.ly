\version "2.19.82"
\language "english"

\include "/Users/rwest/Code/mirrorecho/sandbox/piano/piano_strings_stylesheet.ily"

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
                    \pluckPreSkip
                    \accidentalStyle modern-cautionary
                    s1
                    \pluckShowReson
                                \set glissandoMap = #'(( 0 . 0) ( 0 . 3) ( 1 . 1) ( 1 . 2))
                    \clef "bass"
                    <a,, a,>1
                    \glissando
                    \pluckPreSkip
                    s1
                    \numericTimeSignature
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    \time 7/8
                    <
                        \tweak transparent ##t
                        c
                        \tweak transparent ##t
                        g
                        \tweak transparent ##t
                        c'
                        f'
                    >4.
                    \p
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    <
                        c
                        \tweak transparent ##t
                        a
                        \tweak transparent ##t
                        c'
                        a'
                    >4
                    \glissando
                    \pluckRestEvent
                    r4
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    <
                        \tweak transparent ##t
                        c
                        \tweak transparent ##t
                        a
                        \tweak transparent ##t
                        c'
                        a'
                    >4.
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    <
                        c
                        \tweak transparent ##t
                        g
                        \tweak transparent ##t
                        d'
                        a'
                    >4
                    \glissando
                    \pluckRestEvent
                    r4
                    \numericTimeSignature
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    \time 7/8
                    <
                        \tweak transparent ##t
                        c
                        \tweak transparent ##t
                        g
                        \tweak transparent ##t
                        c'
                        f'
                    >4.
                    \p
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    <
                        c
                        \tweak transparent ##t
                        a
                        \tweak transparent ##t
                        c'
                        a'
                    >4
                    \glissando
                    \pluckRestEvent
                    r4
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    <
                        \tweak transparent ##t
                        c
                        \tweak transparent ##t
                        a
                        \tweak transparent ##t
                        c'
                        a'
                    >4.
                    \glissando
                    \set glissandoMap = #'(( 0 . 0 ) ( 1 . 1 ) ( 2 . 2 ) ( 3 . 3 ))
                    \pluckNoteEvent
                    <
                        c
                        \tweak transparent ##t
                        g
                        \tweak transparent ##t
                        d'
                        a'
                    >4
                    \glissando
                    r4
                }
            }
            \context Staff = "reson_staff"
            \with
            {
                \consists Horizontal_bracket_engraver
            }
            {
                {
                    \crossStaff
                    {
                        \resonPreSkip
                        \accidentalStyle modern-cautionary
                        s1
                        s1
                        s1
                        \numericTimeSignature
                        \resonShow
                        \time 7/8
                        <a,,>4.
                        \p
                        <a,, a,,>4
                        r4
                        <a,,>4.
                        <a,, a,,>4
                        r4
                        \numericTimeSignature
                        \time 7/8
                        <a,,>4.
                        \p
                        <a,, a,,>4
                        r4
                        <a,,>4.
                        <a,, a,,>4
                        r4
                    }
                }
            }
        >>
        \context StaffGroup = "piano_string1"
        <<
            \context Staff = "pluck_staff"
            \with
            {
                \consists Horizontal_bracket_engraver
            }
            {
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