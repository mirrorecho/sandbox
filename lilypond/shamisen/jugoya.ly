\version "2.18.2"

intro = \relative c'' {
	e8\3 e4\3 c\3 c\3 b\3 b\3
}

melody = {

}

jugoyaMusic = {
	\intro
	\melody
}


 \score {
  	%\new Staff {\globalMusic}
    <<
    \new TabStaff 
    	{
			\set Staff.stringTunings = \stringTuning <b' e' b>
    		\jugoyaMusic 
    	}
 	>>
 }