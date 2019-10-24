\version "2.18.2"

% intro = \relative c'' {
%     e8\3 e4\3 c\3 c\3 b\3 b\3
% }

% melody = {

% }

%%http://lsr.di.unimi.it/LSR/Item?id=880

% original code (for zig-zag lines) by Thomas Morley (Harm)
% -> http://lists.gnu.org/archive/html/lilypond-user/2012-12/msg00715.html
% slightly modified to create dashed lines by Paul Morris


lengthenGliss = 
#(define-music-function (parser loation nmbr)(number?)
#{
  \once \override Glissando.springs-and-rods = #ly:spanner::set-spacing-rods
  \once \override Glissando.minimum-length = #nmbr
#})

dashedStaffSymbolLines =
#(define-music-function (parser location dash-space bool-list)
 ((number-pair? '(0.5 . 0.5)) list?)
"
Replaces specified lines of a StaffSymbol with dashed lines.

The lines to be changed should be given as a list containing booleans, with
the meaning:
  #f - no dashes, print a normal line
  #t - print a dashed line
The order of the bool-list corresponds with the order of the given list of
'line-positions or if not specified, with the default.
If the length of the bool-list and the 'line-positions doesn't match a warning
is printed.

The width of the dashes and the spacing between them can be altered by adding a pair
as first argument while calling the function:
\\dashedStaffSymbolLines #'(1 . 1) #'(#f #f #t #f #f)
the first number of the pair is the width, the second the spacing
"
#{
 \override Staff.StaffSymbol.after-line-breaking =
   #(lambda (grob)
     (let* ((staff-stencil (ly:grob-property grob 'stencil))
            (staff-line-positions 
              (if (equal? (ly:grob-property grob 'line-positions) '() )
                '(-4 -2 0 2 4)
                (ly:grob-property grob 'line-positions)))
            (staff-width
              (interval-length
                (ly:stencil-extent staff-stencil X)))
            (staff-space (ly:staff-symbol-staff-space grob))
            (staff-line-thickness (ly:staff-symbol-line-thickness grob))
            ;; width of the dash
            (dash-width (car dash-space))
            ;; space between dashes
            (space-width (cdr dash-space))
            ;; Construct the first dash
            (sample-path `((moveto 0 0)
                           (lineto ,dash-width 0)
                           ))
            ;; Make a stencil of the first dash
            (dash-stencil
              (grob-interpret-markup
                grob
                (markup
                  #:path staff-line-thickness sample-path)))
           ;; width of both dash and space
           (dash-space-width (+ dash-width space-width))
           
           ;; another way: get width of dash from the dash stencil
           ;; (stil-width
           ;;   (interval-length
           ;;     (ly:stencil-extent dash-stencil X)))
           ;; (dash-space-width (+ stil-width space-width))
           
            ;; Make a guess how many dashes are needed.
            (count-dashes
              (inexact->exact
                (round
                  (/ staff-width
                     (- dash-space-width
                        staff-line-thickness)))))
            ;; Construct a stencil of dashes with the guessed count
            (dashed-stil
                (ly:stencil-aligned-to
                  (apply ly:stencil-add
                    (map
                      (lambda (x)
                        (ly:stencil-translate-axis
                          dash-stencil
                          (* (- dash-space-width staff-line-thickness) x)
                          X))
                      (iota count-dashes)))
                  Y
                  CENTER))
            ;; Get the the length of that dashed stencil
            (stil-x-length
              (interval-length
                (ly:stencil-extent dashed-stil  X)))
            ;; Construct a line-stencil to replace the staff-lines.
            (line-stil
              (make-line-stencil staff-line-thickness 0 0 staff-width 0))
            ;; Calculate the factor to scale the dashed-stil to fit
            ;; the width of the original staff-symbol-stencil
            (corr-factor
              (/ staff-width (- stil-x-length staff-line-thickness)))
            ;; Construct the new staff-symbol
            (new-stil
              (apply
                ly:stencil-add
                  (map
                    (lambda (x y)
                      (ly:stencil-translate
                          (if (eq? y #f)
                            line-stil
                            (ly:stencil-scale
                              dashed-stil
                              corr-factor 1))
                          (cons (/ staff-line-thickness 2)
                                (* (/ x 2) staff-space))))
                    staff-line-positions bool-list))))
       
      (if (= (length bool-list)(length staff-line-positions))
        (ly:grob-set-property! grob 'stencil new-stil)
        (ly:warning
          "length of dashed line bool-list doesn't match the line-positions - ignoring"))))
#})



   %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
\layout {
  \context {
    \StaffGroup
    \consists #Span_stem_engraver
  }
}

   %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 \score {
    %\new Staff {\globalMusic}
    <<
    \new StaffGroup 
    <<
    \new Staff \with {
    % \remove "Time_signature_engraver"
    % \hide Clef
    % fontSize = #-3
    % \override StaffSymbol.staff-space = #(magstep -1)
    % \override StaffSymbol.thickness = #(magstep -3)
    
    % \override NoteColumn.glissando-skip = ##t

    %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   \override Glissando.bound-details.left.padding = #0.2 

    %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   \override Glissando.bound-details.right.padding = #0.2 
   
    %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   \override Glissando.bound-details.left.attach-dir = #0 
   
   %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   \override Glissando.bound-details.right.attach-dir = #0 

   % \override Glissando.bound-details.right.attach-dir = #0
   % \override Glissando.bound-details.left.stencil-align-dir-y = #-2
   % \override Glissando.bound-details.right.stencil-offset = #0
   % \once \override Glissando.bound-details.left.Y = #0

   %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   \override Glissando.style = #'dashed-line
   
   %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   \override NoteHead.no-ledgers = ##t 

   % \override NoteHead.stem-attachment = #'(0 . 0)
   % \override NoteColumn.X-offset = #1.0
    }

        {
  % f''4 d \stopStaff
  % \override Staff.StaffSymbol.line-positions = #'(1 3 5 -1 -3)
  % \startStaff g, e |
  % f'4 d \stopStaff
  % \override Staff.StaffSymbol.line-positions = #'(8 6.5 -6 -8 -0.5)
  % \startStaff g, e |
        %     {
            
          %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
          \overrideProperty Score.NonMusicalPaperColumn.line-break-system-details
            #'((Y-offset . 0)
               (alignment-distances . (12)))


            %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            \once \override Score.TimeSignature.stencil = ##f

            \clef "bass_8"

            %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            \set glissandoMap = #'((0 . 0 ) (0 . 1) (1 . 2) (1 . 3))

            <a,,\harmonic a,\harmonic>1


            % \ottava #0

                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                \glissando
            
            % \bar ";"

                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                \stopStaff 
                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                \once \override Staff.Clef.transparent = ##t 
                
                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                \clef percussion 

                % \hide Staff.StaffSymbol

                s1 %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                % \override Staff.StaffSymbol.line-positions = #'(-12 -6 0 6 12)
                % \dashedStaffSymbolLines #'(#t #t #t #t #t)

                % \override Staff.StaffSymbol.line-positions = #'(-12 12)
                % \dashedStaffSymbolLines #'(#t #t)

                % \override Staff.StaffSymbol.line-positions = #'( 0 )
                % \override Staff.StaffSymbol.ledger-positions = #'( 0 0 )


                % \startStaff
                
                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                \stemUp 

                % \hideNotes
                % \grace
                % { <e a d' g'>1 \glissando } 
                

                % \unHideNotes

                % \xNotesOn

                % \lengthenGliss #'10
                % \override Score.Glissando.springs-and-rods =
                %           #ly:spanner::set-spacing-rods
                % \override Glissando.thickness = #2
                % \override Glissando.minimum-length = #4

                %OKOKOKOK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                \set glissandoMap = #'((0 . 0 ) (1 . 1) (2 . 2) (3 . 3))
                <
                \hide e 
                \hide a 
                \hide d' 
                g'
                >8

                \glissando
                
                \once \override NoteColumn.glissando-skip = ##t
                c'8\rest

                \set glissandoMap = #'((0 . 0 ) (1 . 1) (2 . 2) (3 . 3))
                <
                g, 
                \hide a 
                \hide d' 
                \hide g'
                >4
                \glissando

                <
                f 
                \hide a 
                \hide d' 
                \hide a'
                >8

                \glissando

                \once \override NoteColumn.glissando-skip = ##t
                r8 
                \once \override NoteColumn.glissando-skip = ##t
                r4
                
                <
                d 
                \hide a 
                \hide d' 
                \hide a'
                >8

                % c'2\rest 
                % \stopStaff
                % \override Staff.StaffSymbol.line-positions = #'(-22 -21 -20 -19 -18 -17 -16 -15 -14 -13 -12 -11 -10)
                % \startStaff
                c'2\rest d'4 e'4


        % }
    }
    \new Staff \with {
    \remove "Time_signature_engraver"
    % \hide Clef
    % fontSize = #-3
    % \override StaffSymbol.staff-space = #(magstep -2)
    % \override StaffSymbol.thickness = #(magstep -3)
    }

     {

        % \override NoteHead.stem-attachment = #'(0 . 0)

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
        \stemUp
        
        {

          \crossStaff 
        \xNotesOn
        a8 r8 a,4 a,8 
        r8 r4
        \bar "!"

    }
    \autoBeamOn
    }
    >>
 >>
 }