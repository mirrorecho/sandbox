\version "2.18.2"

% -------------------------------------------------------------------------
% include kappa tools:
kappaRoot = "c:/Ran/Code/Kappa/"
\include #(string-append kappaRoot "Kappa.ly")

% -------------------------------------------------------------------------
% TESTING LAYOUT FUNCTIONS.... (doesn't work yet)
%\kappaLayoutTest "solo" "small"
% -------------------------------------------------------------------------

% layout hard-coded here:

kappaStaffSize = #16
#(set-global-staff-size kappaStaffSize )

\paper {
  #(set-paper-size "letter")
  
  #(define fonts
    (make-pango-font-tree "PT Serif"
                          "PT Sans"
                          "PT Mono"
                          (/ kappaStaffSize 20)))
  %;left-margin = 16\mm
  %;system-separator-markup = \slashSeparator
  system-system-spacing #'basic-distance = #9
  system-system-spacing #'padding = #9
}


% -------------------------------------------------------------------------
globalMusic = {s1 s1 s1 s1 s1}

testMusic = \relative c' {
    g1 | 
    \kappaBoxContinue {s32} { c4 e, d'''} {s8.. | s4 s4 s4 s4 | s4 s4}
    g,2 | 
    a4^"Boo!" b c d |
    e1 
  f1 |
}

testMusicTwo = \relative c' {
  s32 \kappaBoxContinue {s8.} {c8 b4 d e8} { s32 | s1 | s2}  g2 |
  a4^"Boo!" b c d |
  e1 |
  g1 |
  f1 |
}



% -------------------------------------------------------------------------
% LILYPOND BOOK / SCORE OUTPUT:
% -------------------------------------------------------------------------

\book {
  \bookOutputName "Kappa Cool"
    \header { 
      title = \markup { \override #'(font-name . "PT Serif Caption") "Kappa Kappa" }
      subtitle = \markup { \override #'(font-name . "PT Serif") "for many instruments" }
      composer = "Randall West"
      tagline = "Copyright 2014 Randall West."
    }
  \score {
    <<
    %\new Staff {\globalMusic}
    \new Staff \with {
        instrumentName = "Violin 1"
        %shortInstrumentName = "Bsn. 1"
      } 
      << \new Voice { \testMusic } \globalMusic >>
    \new Staff \with {
        instrumentName = "Violin 2"
        %shortInstrumentName = "Bsn. 1"
      } 
      << \new Voice { \testMusicTwo } \globalMusic >>
    >>
  }
 }