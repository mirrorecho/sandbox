
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Kappa Functions for Fancy Text Styles:
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%--------------------------------------------------------------------------
% Display text to indicate a duration of time:

kappaTimeSpan = #(define-music-function (parser location text) 
	(string?)
  #{
	\textLengthOn %; does this do anything for marks????
	%;\once \override Score.RehearsalMark #'self-alignment-X = #LEFT
	\once \override Score.RehearsalMark #'X-offset = #2
   %;\mark \markup { \rounded-box \bold $text }
   \mark \markup { \rounded-box $text }
   \textLengthOff
#})

%--------------------------------------------------------------------------
% Big fat rehearsal marks:

kappaRehearsalMark = #(define-music-function (parser location text) 
	(string?)
  #{
   \mark \markup \fontsize #3.3 { \circle \bold $text }
#})

