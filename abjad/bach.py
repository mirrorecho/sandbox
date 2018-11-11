string = """
\version "2.19.54"

\language "english"

\new Score <<

	\new Staff <<
		\key a \major
		\new Voice {
			\voiceOne
			a'2 
			b'4
			cs''4 |
			ds''2 \fermata
			r4
			ds''4 |
			e''4
			b'4
			b'4
			d''4 |
			cs''2. \fermata
			b'4
		}
		\new Voice {
			\voiceTwo
			e'2 
			e'4 
			d'8
			cs'8 |
			gs'2
			r4
			gs'4 |
			gs'4.
			a'8
			gs'8
			fs'8
			g'8
			e'8 |
			a'2.
			gs'4

		}
	>>

	\new Staff <<
		\key a \major
		\new Voice {
			\voiceOne
			cs'2 
			b4 
			fs'4 |
			fs'2
			r4
			bs4 |
			cs8 
			ds'8
			e4
			e4
			e4
			e2.
			e4 |
			
		}
		\new Voice {
			\voiceTwo
		}
	>>

>>
"""

print(string)