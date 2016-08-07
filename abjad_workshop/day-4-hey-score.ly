\version "2.18.2"
\language "english"

\include "day-4-hey-stylesheet.ily"

\new Score <<

	\new StaffGroup = "Hey Staves 1" <<
		\new Staff = "Staff A" {
			a''1 ^\markup { "Up!" } ~	a''1 ~ a''1		
		}
		\new Staff = "Staff B" {
			\timeX
			\repeatBracket "Yo la!" {
				a'2 -\accent (
				b'2 )
			}
			a'2 -\accent (
			b'2 )
			a'2 -\accent (
			b'2 )
		}
	>>

	\new StaffGroup = "Hey Staves 2" <<
		\new Staff = "Staff C" {
			\clef bass
			d4( e f g)
			d4( e f g)
			d4-- e-- f-- g--
		}
		\new Staff = "Staff D" {
			\clef bass
			\times 2/3 {c8-.\accent cs8-. c8 ~ }
			c8 c8-.
			\times 2/3 {c8-.\accent cs8-. c8 ~ }
			c8 c8-.
			|
			c8-.
			c8-.
			c8-.
			c8-.
			c8-.
			c8-.
			c8-.
			c8-.
			|
			\times 2/3 {c8-.\accent cs8-. c8 ~ }
			c8 c8-.
			\times 2/3 {c8-.\accent cs8-. c8 ~ }
			c8 c8-.
		}
	>>

>>