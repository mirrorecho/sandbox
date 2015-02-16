

\include #(string-append kappaRoot "KappaLayout.ly")

% great code for extracting music.... see http://gillesth.free.fr/Lilypond/extractMusic/ or http://lsr.di.unimi.it/LSR/Item?id=542
\include #(string-append kappaRoot "/include/extractMusic.ly")

\include #(string-append kappaRoot "KappaText.ly")
\include #(string-append kappaRoot "KappaTime.ly")
\include #(string-append kappaRoot "KappaBox.ly")
\include #(string-append kappaRoot "KappaSections.ly")


kappaMusicDefaults = {
	\numericTimeSignature
	\override Score.BarNumber #'stencil
    	= #(make-stencil-boxer 0.1 0.25 ly:text-interface::print)
	#(set-accidental-style 'modern)
}





% -----------------------------------------------------------------------
% bartok pizz:
#(define-markup-command (kappaMarkupBartok layout props) ()
  (interpret-markup layout props
    (markup #:stencil
      (ly:stencil-translate-axis
        (ly:stencil-add
          (make-circle-stencil 0.7 0.1 #f)
          (ly:make-stencil
            (list 'draw-line 0.1 0 0.1 0 1)
            '(-0.1 . 0.1) '(0.1 . 1)))
        0.7 X))))

kappaBartok = \markup \kappaMarkupBartok