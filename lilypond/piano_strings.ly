\version "2.18.2"

% intro = \relative c'' {
%     e8\3 e4\3 c\3 c\3 b\3 b\3
% }

% melody = {

% }

\layout {
  \context {
    \StaffGroup
    \consists #Span_stem_engraver
  }
}


 \score {
    %\new Staff {\globalMusic}
    <<
    \new StaffGroup 
    <<
    \new Staff % \with {
    % \remove "Time_signature_engraver"
    % \hide Clef
    % fontSize = #-3
    % \override StaffSymbol.staff-space = #(magstep -1)
    % \override StaffSymbol.thickness = #(magstep -3)
    % }

        {
  % f''4 d \stopStaff
  % \override Staff.StaffSymbol.line-positions = #'(1 3 5 -1 -3)
  % \startStaff g, e |
  % f'4 d \stopStaff
  % \override Staff.StaffSymbol.line-positions = #'(8 6.5 -6 -8 -0.5)
  % \startStaff g, e |
        %     {
            
          \overrideProperty Score.NonMusicalPaperColumn.line-break-system-details
            #'((Y-offset . 0)
               (alignment-distances . (20)))

            \once \override Score.TimeSignature.stencil = ##f

            \clef "bass_8"

            \set glissandoMap = #'((0 . 0 ) (0 . 1) (1 . 2) (1 . 3))



            <a,, a,>1
              
            % \ottava #0

                \glissando
                \stopStaff

                \once \override Staff.Clef.transparent = ##t
                \clef percussion

                s1

                \override Staff.StaffSymbol.line-positions = #'(13 10 7 4 -4 -7 -10 -13)

                % \grace
                % { <d, g, c f>1 } 

                \startStaff
                % \stemDown
                
                \hideNotes

                \grace
                { <d, g, c f>1 } 

                \unHideNotes

                \xNotesOn
                d,8[

                c8\rest ]

                f4 
                g,8
                c'2\rest 
                \stopStaff
                \override Staff.StaffSymbol.line-positions = #'(-22 -21 -20 -19 -18 -17 -16 -15 -14 -13 -12 -11 -10)
                \startStaff
                r2 d'4 e'4
        % }
    }
    \new Staff \with {
    \remove "Time_signature_engraver"
    % \hide Clef
    % fontSize = #-3
    \override StaffSymbol.staff-space = #(magstep -2)
    % \override StaffSymbol.thickness = #(magstep -3)
    }

     {



        \stopStaff
        \once \override Staff.Clef.transparent = ##t
        % \clef percussion
        % \omit Staff.Clef
        
        % \omit Staff.TimeSignature
        % \clef bass

        s1         
        s1 
        \startStaff


        \hideNotes
        \grace { c1 } 
        \unHideNotes
     
        \autoBeamOff   
        \clef bass       
        \crossStaff {
        a,8 r8 a,4 
    }
    \autoBeamOn
    }
    >>
 >>
 }