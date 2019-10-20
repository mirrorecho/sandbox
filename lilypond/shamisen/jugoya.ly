\version "2.18.2"

% intro = \relative c'' {
%     e8\3 e4\3 c\3 c\3 b\3 b\3
% }

% melody = {

% }


 \score {
    %\new Staff {\globalMusic}
    <<
    \new Staff 
        {
  % f''4 d \stopStaff
  % \override Staff.StaffSymbol.line-positions = #'(1 3 5 -1 -3)
  % \startStaff g, e |
  % f'4 d \stopStaff
  % \override Staff.StaffSymbol.line-positions = #'(8 6.5 -6 -8 -0.5)
  % \startStaff g, e |
        %     {
            c1

                \glissando

                s1

                \stopStaff
                \override Staff.StaffSymbol.line-positions = #'(8 6 4 -4 -6 -8)
                \startStaff
                \stemDown
                
                \hideNotes
                \grace
                { e'8 }
                \unHideNotes

                c'8 

                b'8\rest c'4 b'2\rest 
                \stopStaff
                \override Staff.StaffSymbol.line-positions = #'(6 4 -8 -12)
                \startStaff
                r2 d'4 e'4
        % }
    }
    \new Staff {
        \clef bass
        c4
    }
    >>
 }