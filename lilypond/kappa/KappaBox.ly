%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% BOXES YAY!

%%%%%%%%%%%%% 

kappaBoxNotes = #(define-music-function (parser location music)
  (ly:music?)
  #{

    %; Trick to print it after barlines and signatures:
    \once\override BreathingSign #'break-align-symbol = #'custos

    \once \override Staff.TimeSignature.space-alist =
        #'((first-note . (fixed-space . 2.0))
           (right-edge . (extra-space . 0))
           ;; space after time signature ..
           (custos . (extra-space . 0)))

    \once\override BreathingSign #'text = \markup { \filled-box #'(0 . 0.4) #'(-1.4 . 1.4) #0 }
    \once\override BreathingSign #'break-visibility = #end-of-line-invisible
    \once\override BreathingSign #'Y-offset = ##f

    \once\override Staff.BarLine #'space-alist = 
    #'((breathing-sign fixed-space 0)) 

    \breathe 

    $music

    \once\override BreathingSign #'text = \markup { \filled-box #'(0 . 0.4) #'(-1.4 . 1.4) #0 }
    %\once\override BreathingSign #'text = \markup { \fontsize #3 \arrow-head #X #RIGHT ##t }
    \once\override BreathingSign #'Y-offset = ##f

    \breathe

  #})


kappaBoxContinue = #(define-music-function (parser location musicBefore musicDuring musicAfter)
  (ly:music? ly:music? ly:music?)
  #{

    \kappaContinueStart

    $musicBefore 

    \kappaContinueStop

    \kappaBoxNotes $musicDuring

    \kappaContinueStart
    %\kappaRemoveEnd $musicAfter s16 \kappaSixteenthArrow
    $musicAfter
    \kappaContinueStop

  #})


%------------------------------------------------------------------------------------

kappaSixteenthArrow  = {
\once \override Rest  #'stencil = #ly:text-interface::print
\once \override Rest.staff-position = #-2.2
\once \override Rest #'text = \markup {  
  \fontsize #6 {  
      \general-align #Y #DOWN { \arrow-head #X #RIGHT ##t } 
    }
  }
  r16
}

kappaContinueStart = {
    \stopStaff
    %\override Staff.BarLine.bar-extent = #'(-2 . 2)
    \override Staff.StaffSymbol #'line-positions = #'(
        -0.4 -0.3 -.2 -.1 0 .1 .2 .3 .4
        ;-0.5 0
        )
    \startStaff
}

kappaContinueStop = {
    \stopStaff
    \override Staff.StaffSymbol #'line-positions = #'(-4 -2 0 2 4)
    \startStaff
}


%------------------------------------------------------------------------------------


% below is based on based on bracketed passages code here: http://lsr.dsi.unimi.it/LSR/Item?id=377
% decided not to use this, but may return to it....
%{

#(define-markup-command (left-bracket layout props spaceAbove spaceBelow) 
  (number? number?)
(let* ((th 0.1) ;; todo: take from GROB
  (width (* 0.1 th)) ;; todo: take from GROB
  (mySpacing (cons (- 0 spaceBelow) spaceAbove))
  ) ;; todo: take line-count into account
  (ly:bracket Y mySpacing th width)))

#(define-markup-command (right-bracket layout props spaceAbove spaceBelow) 
(number? number?)
(let* ((th 0.1);;todo: take from GROB
(width (* 0.1 th)) ;; todo: take from GROB
  (mySpacing (cons (- 0 spaceBelow) spaceAbove))
  ;(ext '(-4.4 . 4.4))
  ) ;; todo: take line-count into account
  (ly:bracket Y mySpacing th (- width))))

%}