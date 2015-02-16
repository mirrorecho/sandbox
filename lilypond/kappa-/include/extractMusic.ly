%% LSR = http://lsr.dsi.unimi.it/LSR/Item?id=542
% 2013/03/25  : for lilypond 2.16 or higher
% Last modif. :
% - change append! by cons in (make-signature-list)
% - replace moment-null definition, by ZERO-MOMENT, which has been
%   already defined in lily-library.scm.Delete also moment-min definition.
% - Add better support for some special moment value in moment->rhythm.
%   measure-number->moment returns a correct value if \global has an incomplete
%   measure. 

%%%%%%%%% *current-moment* = a global parameter used by extractMusic %%%%%%%%%%%
%% see :     http://www.gnu.org/software/guile/manual/guile.html#SRFI_002d39

#(define (expand-q-chords music); for q chords : see chord-repetition-init.ly
 (expand-repeat-chords! (list 'rhythmic-event) music))

#(use-modules (srfi srfi-39))

#(define *current-moment* (make-parameter ZERO-MOMENT))
%%%%%%%%%%%%%%%%%% some little functions used by extract-range %%%%%%%%%%%%%%%%%
#(define (defined-music? music)
   (not (eq? 'Music (ly:music-property music 'name))))

% a moment<=? is defined in lily-library.scm, but i prefer to use this function
#(define (moment>=? a b)
  (not (ly:moment<? a b)))

% moment-min is defined in lily-library.scm but not moment-max.
#(define (moment-max a b)
    (if (ly:moment<? a b) b a))

#(define (whole-music-inside? begin-music end-music left-range right-range)
	(and (moment>=? begin-music left-range)
		   (moment>=?	right-range end-music )
		   (not (equal? begin-music right-range)))) %% don't take 0-length events
		                        %% (as \override for ex) beginning at right-range
		                        %% (when begin-music = end-music = right-range)
#(define (whole-music-outside? begin-music end-music left-range right-range)
	  (or (moment>=? left-range end-music)
		    (moment>=? begin-music right-range)))

% see duration.cc in Lilypond sources (Duration::Duration)
#(define (moment->rhythm moment)
(let* ((p (ly:moment-main-numerator moment))
	     (q (ly:moment-main-denominator moment))
	     (k (- (ly:intlog2 q) (ly:intlog2 p)))
	     (dots 0))
 (if (< (ash p k) q) (set! k (	1+ k))) ;% (ash p k) = p * 2^k
 (set! p (- (ash p k) q))
 (while (begin (set! p (ash p 1))(>= p q))
    (set! p (- p q))
    (set! dots (1+ dots)))
 (if (> k 6)
   (ly:make-duration 6 0) ; 6 means 64th (max value).
   (let* ((dur (ly:make-duration k dots)) ; no exact duration is suitable for
          (dur-len (ly:duration-length dur)) ; all mom : 2 < 5/4 < 2. So use
          (frac (ly:moment-div moment dur-len))) ; a ratio to adjust dur-len
    (ly:make-duration k dots
       (ly:moment-main-numerator frac) ; frac = 1/1 for moment = 3/4, 7/8 etc ..
       (ly:moment-main-denominator frac))))))

%%%%%  Here are some macros, to keep the extract-range code more compact  %%%%%

#(define-macro (filter-elts-for-non-sequential-music);Chords, SimultaneousMusic
   '(filter defined-music? (map
        (lambda (evt)
          (let ((extracted-evt (extract-range evt from to)))
            (if (equal? (*current-moment*) begin-pos)
                evt ; keeps 0 length events such as scripts, or 'VoiceSeparator.
                (begin
                  (*current-moment* begin-pos) ;% restore *current-moment*
                  extracted-evt))))
         elts)))

#(define-macro (filter-elts-for-sequential-music) ; sequential music
  '(filter defined-music? (map
        (lambda (evt)
          (extract-range evt from to))
        elts)))

      %%%% the big macro for repeated-music %%%%%%%
%{ The extract-repeated-music macro deals with music having the following form :
(make-music 'name
    'elements elts
    'repeat-count n
    'element elt  %%  (make-music 'EventChord ...
%}
#(define-macro (extract-repeated-music)
'(if (not (pair? elts))
  (let* ((unfold-music (make-sequential-music (map
                         (lambda (section) (ly:music-deep-copy elt))
                         (make-list(ly:music-property music 'repeat-count)))))
         (extracted-sections (filter defined-music?
                                     (ly:music-property
                                       (extract-range unfold-music from to)
                                       'elements)))
         (count (length extracted-sections)))
      (case count
        ((0) (make-music 'Music))
        ((1) (car extracted-sections))
        (else   ; the 1st or the last sections has been perhaps shortened
          (let* ((first-section (car extracted-sections))
                 (last-section (car (last-pair extracted-sections))) ; use last instead ?
                 (seq-elts (list #f #f #f));(shortened? count*elt shortened?)
                 (elt-length (ly:music-length elt)))
            (if (ly:moment<? (ly:music-length first-section) elt-length)
                (begin
                  (set! count (1- count))
                  (list-set! seq-elts 0 first-section)))      ; 0 = first elt
            (if (ly:moment<? (ly:music-length last-section) elt-length)
                (begin
                  (set! count (1- count))
                  (list-set! seq-elts 2 last-section)))       ; 2 = 3rd elt
            (cond ((= count 1) (list-set! seq-elts 1 elt))    ; 1 = 2nd elt
                  ((> count 1) (list-set! seq-elts 1
                         (make-music name 'repeat-count count 'element elt))))
            (set! seq-elts (filter (lambda (x) x) seq-elts)) ;delete trailing #f
            (if (= (length seq-elts) 1)
                  (car seq-elts)
                  (make-sequential-music seq-elts))))))
  ; volta-repeat musics use 'element AND 'elements
  (let* ((extracted-elt (extract-range (ly:music-deep-copy elt) from to))
         (extracted-elts (filter defined-music? (map
                           (lambda (section)(extract-range section from to))
                           elts))))
    (cond ((not (defined-music? extracted-elt))
              (case (length extracted-elts)
                ((0) (make-music 'Music))
                ((1) (car extracted-elts))
                (else (make-sequential-music extracted-elts))))
          ((null? extracted-elts) extracted-elt)
          (else (make-sequential-music (cons extracted-elt extracted-elts)))))
;         (else (make-music name 'repeat-count(+
;                                       (ly:music-property music 'repeat-count)
;                                       (length extracted-elts)
;                                       (- (length elts)))
;                                  'element extracted-elt
;                                  'elements extracted-elts))))
))

                %%%%%% the extract-range function %%%%%%%
%%% This function cannot be used directly : *current-moment* has to be
%%% initialized before. You can get also some strange behaviour, if you
%%% don't use (ly:music-deep-copy). Use extract-during below, instead.
#(define (extract-range music from to)
"Keeps only music beetween `from and `to, `from and `to as moment"
(let ((begin-pos (*current-moment*))
(end-pos (ly:moment-add (*current-moment*) (ly:music-length music))))
 (*current-moment* end-pos) ;for the next music to process
 (cond
  ((whole-music-inside? begin-pos end-pos from to) music)
  ((whole-music-outside? begin-pos end-pos from to)(make-music 'Music))
        ; From this point, the intervals [begin-pos end-pos][from to] overlaps
  (else
    (let((name (ly:music-property music 'name)))
     (if (ly:duration? (ly:music-property music 'duration))
       (begin    ; a NoteEvent, a skip, a rest, a multiRest
         (set! begin-pos (moment-max begin-pos from))
         (set! end-pos (moment-min end-pos to))
         (ly:music-set-property! music 'duration 
            (if (memq name (list 'NoteEvent 'RestEvent))
              (moment->rhythm (ly:moment-sub end-pos begin-pos))
              (make-duration-of-length (ly:moment-sub end-pos begin-pos)))))
                ; for containers of duration evt, or a chord
       (let ((elts (ly:music-property music 'elements))
             (elt  (ly:music-property music 'element)))
          (*current-moment* begin-pos)    ;we go deeper into the same music evt
          (cond
            ((string-contains (symbol->string name) "RepeatedMusic")
                ; repeated-musics have a complexe behaviour
                (set! music (extract-repeated-music))) ;; see macros above
            ((ly:music? elt)(ly:music-set-property! music 'element
                    (extract-range elt from to)))
            ((pair? elts)(ly:music-set-property! music 'elements
                (if (memq name (list 'SimultaneousMusic 'EventChord))
                    (filter-elts-for-non-sequential-music) ;; see macros
                    (filter-elts-for-sequential-music)))))
          (*current-moment* end-pos))) ; next music evt
     music))
)))

%% Before defining the music-function \extractMusic, we define a helpful
%% function \upToMeasure, to let the user define the `from and `during
%% parameter of \extractMusic, using the number of the measure.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% upToMeasure %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% upToMeasure is an user-function that can be used inside extractMusic params.%
#(define (make-signature-list)      ; used by measure-number->moment
"Make a list of lists of moments with
  1st element = length of one measure in the current time-signature,
  2nd element = length, between 2 time-signatures.
  3rd element = like 2nd but ignores \\partial and Timing.measurePosition sets
All the signature-changes, and manual timing changes have to be set in the music
variable \\global"
(if (or (not (defined? 'global)) (not (ly:music? global)))
  (list (list (ly:make-moment 1 1) ZERO-MOMENT ZERO-MOMENT)); 4/4 during 0
  (let ((signature-list '())
        (local-pos ZERO-MOMENT) ;; "distance" from the prev signature change
        (to-add #f) ;; for info3 if \partial or \set Timing.measurePosition
        (prev-measure-len (ly:make-moment 1 1))) ;; the prev section length
     (define (add-info info1 info2 info3)
        (set! signature-list (cons (list info1 info2 info3) signature-list)) 
        (set! local-pos ZERO-MOMENT)             ;; re-init for the next section
        (set! to-add #f))
     (define (do-make evt)   ; an inner-function
        (let ((name (ly:music-property evt 'name)))
           (cond
              ((eq? name 'TimeSignatureMusic);; a time signature change occurs
                  (let ((measure-len (ly:make-moment
                            (ly:music-property evt 'numerator)
                            (ly:music-property evt 'denominator))))
                    (if (ly:moment<? ZERO-MOMENT local-pos) ; local-pos = 0 ?
                          (add-info prev-measure-len local-pos (if to-add
                                   (ly:moment-add local-pos to-add) local-pos)))
                    (set! prev-measure-len measure-len)))
              ((or (ly:duration? (ly:music-property evt 'duration))
                   (eq? name 'EventChord))
                (let ((new-pos (ly:moment-add local-pos (ly:music-length evt))))
                  (if (or (not to-add)         ; not manual timing set ?
                          (ly:moment<? (ly:moment-sub new-pos local-pos)
                                       to-add)); to-add =? new-pos - local-pos
                    (set! local-pos new-pos)   ; common process
                    (add-info prev-measure-len ; add an info entry
                              new-pos
                              (ly:moment-add new-pos to-add)))))
              ((eq? name 'PartialSet) ;; \partial
                  (let ((offset (ly:duration-length
                                   (ly:music-property evt 'partial-duration))))
                     (set! local-pos (ly:moment-sub local-pos offset))
                     (set! to-add offset)))
              ((and (eq? name 'PropertySet) ;; \set Timing.measurePosition
                    (eq? (ly:music-property evt 'symbol) 'measurePosition))
                  (let ((offset (ly:music-property evt 'value))) ; offset < 0
                     (set! local-pos (ly:moment-add local-pos offset))
                     (set! to-add (ly:moment-sub ZERO-MOMENT offset))));=-offset
              (else
                  (let ((elt (ly:music-property evt 'element))
                        (elts (ly:music-property evt 'elements))
                        (count (ly:music-property evt 'repeat-count)))
                     (cond          ;all repeated-music but volta-repeats
                        ((and (integer? count)
                              (null? elts)
                              (not (eq? name 'VoltaRepeatedMusic)))
                            (do-make
                                 (make-sequential-music (make-list count elt))))
                        ((eq? name 'SimultaneousMusic)
                            (let ((save-pos local-pos)
                                  (max-pos local-pos))
                                (for-each
                                  (lambda(x)
                                      (do-make x)
                                      (if (ly:moment<? max-pos local-pos)
                                          (set! max-pos local-pos))
                                      (set! local-pos save-pos))
                                  elts)
                               (set! local-pos max-pos)))
                        (else    ; all sequential-musics, volta-repeats etc ...
                            (if (ly:music? elt) (do-make elt))
                            (if (pair? elts) (for-each do-make elts)))))))))
     (do-make (ly:music-deep-copy global)) ;;
     (reverse				;; return in the right order, but before :
       (cons (list prev-measure-len     ;; add the timing info of the last section.
                   local-pos
                   (if to-add (ly:moment-add local-pos to-add)
                              local-pos))
             signature-list))))) 

%% measure-number->moment is used by \upToMeasure
%% the optional arg is used in arranger.ly (in function pos->moment)
%% the difficulty here is to deal with \partial and \set Timing.measurePosition :
%% \extractMusic works with moments as ly:music-length, and doesn't care of theses
%% manual tweaks, but bar numbering does.
#(define* (measure-number->moment number #:optional first-measure)
"Give the length of the music, before the measure number `number"
(let ((current-measure-number (or first-measure 1))
      (current-moment ZERO-MOMENT)
      (signature-list (make-signature-list))) ; see make-signature-list above
 ;; (format #t "\nsignature-list : ~a\n" signature-list)
 (let loop ((pointer-list signature-list))
   (if (pair? pointer-list)              ; each elt of signature-list is a
       (let* ((info (car pointer-list))  ; list (info) describing each sections.
              (1measure-len (first info)); the len of 1 measure
              (nmeasures-len (second info)); len between 2 time signatures
              (real-len (third info)) ; idem but ignores manual timing tweaks
              (mom (ly:moment-div nmeasures-len 1measure-len)); the denominator
                   ;  should be 1, but assume an incomplete section (last measure ?) :
              (n (/ (ly:moment-main-numerator mom)     ; n = number of measures
                    (ly:moment-main-denominator mom))) ; in the current section
              (timing-end (ly:moment-add current-moment nmeasures-len)))
           (set! current-measure-number (+ current-measure-number n))
           (set! current-moment (ly:moment-add current-moment real-len))
           (cond
              ((< current-measure-number number)(loop (cdr pointer-list)))
              ((= current-measure-number number)) ; do nothing
              (else (let ((delta (- current-measure-number number))
                          (num (ly:moment-main-numerator 1measure-len))
                          (denom (ly:moment-main-denominator 1measure-len)))
                       (set! current-moment (ly:moment-sub     ; move backward !
                                  timing-end
                                  (ly:make-moment (* delta num) denom)))))))
       ; Here, \global has less measures than `number. Continue according to the
       (let* ((last-1measure-len (first (last signature-list))) ; last signature
              (delta (- number current-measure-number))
              (num (ly:moment-main-numerator last-1measure-len))
              (denom (ly:moment-main-denominator last-1measure-len)))
           (set! current-moment (ly:moment-add
                                       current-moment
                                       (ly:make-moment (* delta num) denom))))))
  current-moment))

% TO DO : find a better name for `upToMeasure
upToMeasure = #(define-music-function (parser location number) (integer?)
"Return a skip music, which has the same length than the music before measure
`number, according to the time-signature informations given by \\global."
(let((up-to-moment (measure-number->moment number)))
  ;;(display up-to-moment)(newline)
  (make-music 'SkipEvent
          'tags (list 'from-measure-one ) ; to detect from where it is relative.
          'duration  (make-duration-of-length up-to-moment))))
                      %%% check-length %%%
% The eventual use of \upToMeasure in the `during  parameter of the
% extractMusic function implies a special check.
#(define (check-length from-moment during)
"Returns a valid length of `during, detecting if this music `during is relative
to the first measure or to `from-moment, and checking if the measure
given by the user is not before `from-moment."
(let* ((error-found #f)
       (good-during (music-map
         (lambda (evt)
             (if (memq 'from-measure-one (ly:music-property evt 'tags))
               (let ((len (ly:moment-sub (ly:music-length evt) from-moment)))
                  (if (ly:moment<? len ZERO-MOMENT)
                      (set! error-found #t)
                      (set! evt (make-music 'SkipEvent 'duration
                                        (make-duration-of-length len))))))
             evt)
         (ly:music-deep-copy during))))
 (if error-found #f (ly:music-length good-during))))

%%%%%%%%%%%%%%%%%%%%%%%%%%    extractMusic     %%%%%%%%%%%%%%%%%%%%%%%%%%%%
extractMusic = #(define-music-function (parser location music from during)
								                                 (ly:music? ly:music? ly:music?)
"Copy all events of `music, if their position relative to the begin of
`music, is higher than the length of `from music and lower that the sum of the
length of `from music and the length of `during music."
 (let* ((from-length (ly:music-length from))
        (during-length (or (check-length from-length during)
                           ZERO-MOMENT))
        (to-length (ly:moment-add during-length from-length)))
    (parameterize ((*current-moment* (ly:make-moment 0 1)))
             (extract-range (expand-q-chords music) from-length to-length))))


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% derived functions %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% used by \extractEnd
#(define (mmR-or-mmS? music) ; Is music = \mmR or \mmS ? (see later)
  (let ((res #f))
    (music-map
       (lambda (evt)    ; \mmR and \mmS have an evt with \tag #'mmWarning
          (if (memq 'mmWarning (ly:music-property evt 'tags))
             (set! res #t))
          evt)
       music)
    res))

extractBegin = #(define-music-function (parser location music during)
															                             (ly:music? ly:music?)
 #{ \extractMusic $music s1*0 $during #})
 %%%%%%%%

extractEnd = #(define-music-function (parser location music from)
															                             (ly:music? ly:music?)
(let ((during (cond
        ((not (mmR-or-mmS? music)) ;; to extract until the end of the music, set
            mmS)        ;; the during parameter as a quasi-infinite length music
        ((and (defined? 'global)(ly:music? global))
            #{ \tag #'from-measure-one \global #})
        (else                     ;; music is \mmR or \mmS and no \global found
           (ly:music-message music (string-append
              "\n\\mmR and \\mmS are too long to be extracted until their end !"
              "\nPlease, set the length of your music in a \\global variable."))
            #{ s1*0 #}))))       ;; avoid a quasi-infinite length extraction ...
 #{ \extractMusic $music $from $during #}))

 %%%%%%%%

insertMusic = #(define-music-function
  (parser location music where musicToInsert)(ly:music? ly:music? ly:music?)
 #{
	\extractBegin $music $where
	$musicToInsert
	\extractEnd $music $where
 #})
 %%%%%%%%

replaceMusic = #(define-music-function 
   (parser location music where replacementMusic)(ly:music? ly:music? ly:music?)
#{
	\extractBegin $music $where
        $replacementMusic
	\extractEnd $music { $where $replacementMusic }
#})
 %%%%%%%%

replaceVoltaMusic = #(define-music-function (parser location
        musicWithVoltas where replacementMusic) (ly:music? ly:music? ly:music?)
(let ((global-struct (map-some-music                ;; see music-functions.scm     
        (lambda (evt)                               ;; skipifies musicWithVoltas
          (let ((dur (ly:music-property evt 'duration)))
             (cond ((ly:duration? dur)
                      (make-music 'SkipEvent 'duration dur))
                   ((eq? 'EventChord (ly:music-property evt 'name))    
                      (make-music 'SkipEvent 'duration (duration-of-length
                          (ly:music-length evt))))
                   (else #f))))
        (ly:music-deep-copy musicWithVoltas))))
   #{
     <<
      \replaceMusic $musicWithVoltas $where $replacementMusic
      $global-struct
     >>
   #}
))

 %%%%%%%%
#(define (extract-during music from during)
"A scheme implementation of extractMusic function, `from and `during as moment."
	(parameterize ((*current-moment* (ly:make-moment 0 1)))
    (extract-range (expand-q-chords (ly:music-deep-copy music))
                   from
                   (ly:moment-add from during))))

%%%%%%%%%%%%%%%%%%%%% multiExtractMusic %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
#(define (clean-music mus)
"Try to reduce the number of sequential music"
(let ((name (ly:music-property mus 'name)))
  (cond
    ((eq? name 'SequentialMusic)
       (ly:music-set-property! mus 'elements (fold-right
          (lambda (evt prev-list)
            (if (eq? (ly:music-property evt 'name) 'SequentialMusic)
              (append (ly:music-property (clean-music evt) 'elements) prev-list)
              (cons (clean-music evt) prev-list)))
          '()
          (ly:music-property mus 'elements))))
    ((eq? name 'SimultaneousMusic)
       (ly:music-set-property! mus 'elements
                (map clean-music (ly:music-property mus 'elements))))
    ((memq name (list 'RelativeOctaveMusic 'UnrelativableMusic))
       (ly:music-set-property! mus 'element (clean-music
                (ly:music-property mus 'element)))))
 mus))

#(define (clean-seq-elts elements)
"Try to make a big list with no containers,with only chords"
(ly:music-property
  (clean-music (make-sequential-music elements))
  'elements))

  %% the function make-mEM-func below allows the user to automatically convert
  %% a function to be used in the seq-music parameter of multiExtractMusic. 
  %% The new function will have the same name but with the prefix mEM.
  %% See explanations of multiExtractMusic
#(define functionsTagsAlist '())
#(define (make-mEM-func func-symbol)
(if (symbol? func-symbol)
  (let* ((new-symb (symbol-append 'mEM func-symbol))
         (tag-symbol (symbol-append new-symb 'Tag))
         (new-func (define-music-function (parser location param1 param2)
													                                (ly:music? ly:music?)
										   (make-music 'SequentialMusic 'elements
										           (list param1 param2) 'tags (list tag-symbol)))))
    (ly:parser-define! parser new-symb new-func)
    (set! functionsTagsAlist
              (cons `(,tag-symbol . ,func-symbol) functionsTagsAlist)))))

%%%%%%%%%%
%% how to use multiExtractMusic : %%%%%%%
%{ 
\multiExtractMusic from seq-music
Make a series of extractions. `seq-music	must be a sequential music
of the following form :
 {  \musicA  \duringA
    \musicB  \duringB
 	etc...}
The result will be a sequence of this form {resultA resultB etc...} with
 resultA = \extractMusic \musicA \from                     \duringA
 resultB = \extractMusic \musicB {\from \duringA}          \duringB
 resultC = \extractMusic \musicC {\from \duringA \duringB} \duringC
 etc ...
 So the `from parameter of each \extractMusic is automatically computed by
 adding the precedent length.
The last during element can be omitted. If so, the music is extracted up to the
end of the music element.
Setting a during element to a 0 length music, let the \music untouched
(i.e not extract). Ex (in 4/4):
\multiExtractMusic s1*5 { %( extraction will begin measure 6 (5 measures length)
	\vlI s1*2    %% extract 2 measures from measure 6 of vlI part
	R1*3 s1*0    %% add 3 whole-rest
	\vlII s1*9   %% add 9 measures beginning at measure 11 (= 5 + 2 + 3 length)
				  %% from vlII
}

If you have a function with 2 music parameters,  you can create a new one using
make-mEM-func, to be used inside the seq-music parameter of multiExtractMusic.
The new function will have the same name but with the prefix mEM.
The new function will have the same behaviour, but his first parameter will be 
first extracted :
\multiExtractMusic s1*0 { 
  \func \musicA \paramA     \duringA  % apply func to musicA and paramA
                                      % then extract the result during duringA
  \mEMfunc \musicB \paramB  \duringB  % First extract musicB during duringB
                                      % then apply func to the result and paramB
}
%} %%%%%

multiExtractMusic = #(define-music-function (parser location from seq-music)
                                                        (ly:music? ly:music?)
(let* ((from-length (check-length ZERO-MOMENT from))
       (elts (ly:music-property seq-music 'elements))
       (len (length elts))
       (n  (+ (quotient len 2) (remainder len 2))) ; len = 9 => n = 9/2 + 1 = 5
       (result-list (make-list n)))
 (if (not (and (pair? result-list)
               (eq? 'SequentialMusic (ly:music-property seq-music 'name))))
   (ly:input-message location
              "Invalid seq-music parameter! Not a sequential music, or empty.")
   (ly:music-set-property! seq-music 'elements (clean-seq-elts (map
     (lambda (x)
       (let* ((music (car elts))
              (next-pair (cdr elts))
              (mEM-func? (lambda (m)
                           (let ((tags (ly:music-property m 'tags)))
                              (and (pair? tags)
                                   (assq (car tags) functionsTagsAlist)))))
              (entry->mEM-func (lambda (entry param1 param2)
                                 (let ((func (ly:music-function-extract
                                                 (primitive-eval (cdr entry)))))
                                    (func parser location param1 param2)))))
         (if (pair? next-pair)
           (let ((during-length (check-length from-length (car next-pair))))
              (set! music (cond
                ((equal? during-length #f) (make-music 'Music))
                ((equal? during-length ZERO-MOMENT)
                    (if (mmR-or-mmS? music) #{ s1*0 #} music))
                (else
                  (let ((entry (mEM-func? music)))
                    (if entry
                       (let*((es (ly:music-deep-copy
                                     (ly:music-property music 'elements)))
                             (param1 (extract-during (car es)
                                              from-length during-length))
                             (param2 (cadr es)))
                          (entry->mEM-func entry param1 param2))
                       (extract-during music from-length during-length))))))
              (set! elts (cdr next-pair))
              (set! from-length (ly:moment-add from-length
                                               (ly:music-length music))))
           (set! music       ; the last `during parameter has been omitted
               (let ((skip (make-event-chord (list
                              (make-music 'SkipEvent 'duration
                                 (make-duration-of-length from-length)))))
                     (entry (mEM-func? music)))
                 (if entry
                       (let*((es (ly:music-property music 'elements))
                             (elt (car es))
                             (param1 #{ \extractEnd $elt $skip #})
                             (param2 (cadr es)))
                          (entry->mEM-func entry param1 param2))
                       #{ \extractEnd $music $skip #}))))
         music))
     result-list))))
 seq-music))

%%%%%%%%%%%%%%%%%%%%%

multiReplaceMusic =#(define-music-function (parser location music seq-music)
                                                        (ly:music? ly:music?)
(let ((res music))
  (if (eq? 'SequentialMusic (ly:music-property seq-music 'name))
    (let loop ((pointer-list (ly:music-property seq-music 'elements)))
      (if (pair? pointer-list)
        (let* ((replacement (car pointer-list))
               (next (cdr pointer-list))
               (where (and (pair? next)
                           (car next))))
          (if where (begin
            (set! res #{ \replaceMusic $res $where $replacement #})
               ; ((ly:music-function-extract replaceMusic) ; => \replaceMusic
                ;      parser location res where replacement))
            (loop (cdr next))))))))
  (clean-music res)))
%%   (clean-music res)))
% (make-sequential-music (clean-seq-elts (list res)))))

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%% shortcuts %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% 2 shortcuts wich can be usefull in the `music parameter of extractBegin,
%% when there is a lot of time-signature changes. For ex :
%     \extractBegin \mmR \upToMeasure #25
%  will behave like an automatic multiMeasureRest filler.
                    %%% mmRest %%%
mmR = { R1*1000000 \tag #'mmWarning R1 } % The \tag is a way to be recognized
                    %%% mmSkip %%%
mmS = { s1*1000000 \tag #'mmWarning s1 }
 %%%%%%%%


#(define eM extractMusic)
#(define M upToMeasure)
#(define eB extractBegin)
#(define eE extractEnd)
#(define iM insertMusic)
#(define rM replaceMusic)
#(define mEM multiExtractMusic)
#(define mRM multiReplaceMusic)