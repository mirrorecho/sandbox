
\version "2.18.2"
\language "english"

#( set-default-paper-size "letter" 'landscape )
#( set-global-staff-size 12 ) % default is 16

#(define (text-spanner-start-stop mus)
    (let ((elts (ly:music-property mus 'elements)))
    (make-music 'SequentialMusic 'elements 
       (append  
          (list (make-music 'TextSpanEvent 'span-direction -1))
          (reverse (cdr (reverse elts)))
          (list (make-music 'TextSpanEvent 'span-direction 1))
          (list (last elts))))))

arrowSpanner =
#(define-music-function (parser location glyph num den music)
   (string? string? string? ly:music?)
  #{
	\override TextSpanner.bound-padding = #1.0
	\override TextSpanner.style = #'line
	\override TextSpanner.bound-details.right.arrow = ##t
	\override TextSpanner.bound-details.left.text = \markup { \column { \musicglyph $glyph \vspace #0 $num \vspace #0 "-"  \vspace #0 $den } }
	% \override TextSpanner.bound-details.right.text = #"gag"
	\override TextSpanner.bound-details.right.padding = #0.6

	\override TextSpanner.bound-details.right.stencil-align-dir-y = #CENTER
	\override TextSpanner.bound-details.left.stencil-align-dir-y = #CENTER
  
     $(text-spanner-start-stop music)
  #})


\layout {
	% indent=0
}

\paper {
	left-margin = 1\in 
	tagline = "HELLO!"
	top-margin = 1\in
}

\new Score \with {
		\override BarLine.bar-extent = #'(0 . 0)
		\override Bream.breakable = ##t
		\override Glissando.breakable = ##t 
		\override SpacingSpanner.strict-grace-spacing = ##t 
		\override SpacingSpanner.strict-note-spacing = ##t
		\override SpacingSpanner.uniform-stretching = ##t
		\override TimeSignature.X-extent = ##f
		\override TimeSignature.Y-offset = 10
		\override TupletNumber.text = #tuplet-number::calc-fraction-text
		autoBeaming = ##f
		% proportionalNotationDuration = #(ly:make-moment 1 64)
	} <<
	\new PianoStaff \with {
		\override StaffGrouper.staff-staff-spacing.minimum-distance = 18
	} <<
	% RH Staff (including time signatures)
	\new Staff \with {
		% \remove Time_signature_engraver
		\override Clef.stencil = ##f
		\override NoteHead.stencil = ##f
		\override StaffSymbol.line-positions = #'( 0 )
		instrumentName = \markup { \hcenter-in #6 RH }
	} <<
		% SPECIAL TIME SIGNATURE Voice
		\new Voice \with {
			\remove Forbid_line_break_engraver
		} {
			\time 9/16
			s1 * 9/16
			\time 7/16
			s1 * 7/17
		}
		% VOICE for the RH music
		\new Voice \with { 
			\override Beam.positions = #'(-6 . -6)
			\override DynamicLineSpanner.staff-padding = 9
			\override Glissando.bound-details.left.padding = 0.25
			\override Glissando.bound-details.left.start-at-dot = ##f
			\override Glissando.thickness = 6
			\override Stem.length = 10

			\remove Forbid_line_break_engraver
		} {
			\time 9/16
			\arrowSpanner "scripts.downbow" "0" "0" { b'8[ \glissando \f \< } 
			\arrowSpanner "" "0" "0" { b'8 \glissando } 
			\arrowSpanner "scripts.downbow" "0" "0" { b'16 \glissando } 
			\arrowSpanner "scripts.downbow" "0" "0" { b'16] \glissando } 
			\arrowSpanner "scripts.downbow" "0" "0" { b'8[ \glissando } 
			\arrowSpanner "scripts.downbow" "0" "0" { b'8 \glissando } 
			\arrowSpanner "scripts.downbow" "0" "0" { b'16 \glissando } 
			\arrowSpanner "scripts.downbow" "0" "0" { b'16] \glissando } 
			\times 2/3 { 
				\arrowSpanner "scripts.downbow" "0" "0" { b'4 \glissando \ff \> }
				b'8 \f
			}
		}
	>>
	% LH STAFF
	\new Staff \with {
		\remove Time_signature_engraver
		instrumentName = \markup { \hcenter-in #6 LH }
	} \new Voice \with { 
			\override Glissando.thickness = 3
			\remove Forbid_line_break_engraver
		} {
		\clef bass
		\times 2/3 {
				d'8	\glissando
				cs'1 \glissando
				ef'4 \glissando
				d'8 
			}
	}
	>>
>>