
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Kappa Functions for time/meter stuff (esp. unmetered music)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

#(load (string-append kappaRoot "KappaTime-Scheme.scm"))

%--------------------------------------------------------------------------

%#(define kappaIsMetered #t) % I guess this worked at some point....?
%#(ly:add-option 'kappaIsMetered #t "Whether time is metered or not.")

%#(define make-variable )
%#(variable-set! kappaIsMetered #f)

%--------------------------------------------------------------------------
% X/X Time Signature for unmetered music


kappaTimeXMarkup = \markup { \override #'(baseline-skip . 0.5) \column { "X" "X"} }

kappaTimeX = {
		%\override Staff.TimeSignature #'style = #'default %is this needed?
  		\override Staff.TimeSignature #'stencil = #(lambda (grob)
			(parenthesize-stencil (grob-interpret-markup grob kappaTimeXMarkup) 0.1 0.4 0.4 0.1 ))
  		\time 1000/1
}

% (return to normal time signature)
kappaTimeMeter = #(define-music-function (parser location time-sig-fraction) 
	(pair?)
  #{
  		\override Staff.TimeSignature #'stencil = #'default
  		\time $time-sig-fraction
#})


%--------------------------------------------------------------------------
% increase bar number
% FUNCTION FOR INCREASING BAR NUMBER BASED ON: http://lsr.di.unimi.it/LSR/Snippet?id=333

kappaAddBarNumber =  \applyContext
#(lambda (x)
  (let ((measurepos (ly:context-property x 'measurePosition)))
   ; Only increase bar number if not at start of measure.
   ; This way we ensure that you won't increase bar number twice
   ; if two parallel voices call increaseBarNumber simultanously:
   (if (< 0 (ly:moment-main-numerator measurepos)) ; ugh. ignore grace part
    (begin
     (ly:context-set-property!
      (ly:context-property-where-defined x 'currentBarNumber)
      'currentBarNumber
      (1+ (ly:context-property x 'currentBarNumber)))
     ; set main part of measurepos to zero, leave grace part as it is:
     (ly:context-set-property!
      (ly:context-property-where-defined x 'measurePosition)
      'measurePosition
      (ly:make-moment 0 1
       (ly:moment-grace-numerator measurepos)
       (ly:moment-grace-denominator measurepos)))))))


%--------------------------------------------------------------------------
% bar lines

kappaXBar = {
  \kappaAddBarNumber
  \bar "!"
}

% BELOW ARE VARIOUS ATTEMPTS TO GET "|" WORKING FOR BOTH METERED AND UNMETERED SECTIONS... WOULD LIKE TO RETURN TO THIS
%{
 setMeterStatus = #(define-void-function (parser location meter-status)
     (boolean?)
   (ly:add-option 'kappaIsMetered meter-status "BOO")
) 
%}

kappaBar = #(define-music-function (parser location) 
  ()
    (if (kappaGetMetered) 
      #{\bar "|" #} 
      #{\kappaXBar #}
    )
)

"|" = \kappaBar % nice to be able to use normal | symbol

    %{
    #(if (ly:get-option 'kappaIsMetered)
      \bar "|"
      {
        \kappaAddBarNumber   
        \bar "!"
      }
      #)
    %}

%{
kappaDefaultBarType = \applyContext
  #(lambda (x)
    (format #t "~a"
     (ly:context-property x 'defaultBarType)))
%}

%"|" = #(if (eqv? (ly:get-option 'kappaIsMetered)) #{\bar "|" #} #{\bar "||" #})


%--------------------------------------------------------------------------
% turning meter on and off (bar lines and time signatures both)



kappaMeterOff = #(define-music-function (parser location)
  ()
#{
  #(kappaSetMetered #f)
  #(set-accidental-style 'forget)
  %;\setMeterStatus ##f
  %;\set Timing.defaultBarType = "!"
	%;\set Timing.defaultBarType = "kappaTimeXBar"
	%;\set Timing.timing = ##f
  \bar "||"
  \kappaTimeX
#})

kappaMeterOn = #(define-music-function (parser location time-sig-fraction) 
	(pair?)
  ;(ly:set-option 'kappaIsMetered #t)
    #{
      %;#(kappaSetMetered #t)
      %;#(set-accidental-style 'modern)
      %;\setMeterStatus ##t
  		%;\kappaAddBarNumber
  		%;\cadenzaOff
  		%;\set Timing.defaultBarType = "|"
      %;\set Timing.timing = ##t
  		%;\bar "||"
      \kappaAddBarNumber
  		\kappaTimeMeter $time-sig-fraction
      #( set-accidental-style 'modern )
#})

%--------------------------------------------------------------------------
% unmetered full-bar rests

kappaXBarRest = {
	s1 r1-\parenthesize \fermata
}

%--------------------------------------------------------------------------
% functions to remove start or end of music: see http://lists.gnu.org/archive/html/lilypond-user/2013-04/msg00172.html


kappaRemoveStart = #(define-music-function (parser location music remove)
  (ly:music? ly:music?)
#{ \extractMusic $music $remove \mmR #} )

kappaRemoveEnd = #(define-music-function (parser location music remove)
      (ly:music? ly:music?)
      (let*
       ((new-length (ly:moment-sub (ly:music-length music) (ly:music-length
        remove)))
 (newskip (make-music 'SkipEvent 'duration (make-duration-of-length
new-length))))
#{ \extractMusic $music <> $newskip #} 
))


%--------------------------------------------------------------------------
% these look beeter but create problems... they create havoc if at the beginning of an unmetered section 
% and mess up the unmetered bar numbering (not sure why)
%{
kappaXBarRest = #(define-music-function (parser location music) 
	(ly:music?)
  (let ((lst (list
        #{
        	\set Timing.measureLength = #(ly:music-length music)
          $(mmrest-of-length music)
          \unset Timing.measureLength
        #})))
 (make-sequential-music lst))

)
%}

%{
kappaTimeXRest = #(define-music-function (parser location fermata? music) 
	(boolean? ly:music?)

  (let ((fermata (make-music 'MultiMeasureTextEvent
                             'tweaks (list
                                      ;; Set the 'text based on the 'direction
                                      (cons 'text (lambda (grob)
                                                    (if (eq?
(ly:grob-property grob 'direction) DOWN)
                                                        (markup
#:musicglyph "scripts.dfermata")
                                                        (markup
#:musicglyph "scripts.ufermata"))))
                                      (cons 'outside-staff-priority 40)
                                      (cons 'outside-staff-padding 0))))
        (lst (list
        #{
          \set Timing.measureLength = #(ly:music-length music)
          $(mmrest-of-length music)
          \unset Timing.measureLength
        #})))
 (make-sequential-music
  (if fermata?
  (cons fermata lst)
  lst))))
  %}

  %--------------------------------------------------------------------------