\version "2.18.2"
\language "english"

globals = {
	\time 3/4
	\key g \major
	\partial 4
}


\new PianoStaff <<
	\new Staff 
		<<
			{
			\globals
			g'4 | g'2 d''4 | b'4. a'8 g'4 | g'4. a'8 b'4 | a'2\fermata b'4 | d''2 c''4 | b'4 a'2 | g'2\fermata \bar ":|."
			}
		\\
			{
			d'4 | d'4 e'4 d'4 | d'2 b4 | e'8 d'8 e'8 fs'8 g'4 | fs'2 g'4 | d'4 e'4 fs'4 | g'2 fs'4 | d'2 \bar ":|."
			}		
	>>
	\new Staff <<
		{
			\globals
			\clef bass
			b4 | b4 c'8 b8 a4 | g4 fs4 g4 | c'8 b8 c'4 d'4 | d'2 d'4 | a4 b4 c'4 | d'4 e'4 d'8 c'8 | b2 \bar ":|."
		}
		\\
		{
			\globals
			\clef bass
			g,4 | g4 e4 fs4 | g4 d4 e4 | c4 b,8 a,8 g,4 | d2 g,4 | fs,4 g,4 a,4 | b,4 c4 d4 | g,2 \bar ":|."
		}
	>>
>>