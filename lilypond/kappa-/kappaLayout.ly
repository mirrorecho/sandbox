
%--------------------------------------------------------------------------
% sets paper and other settings for various layouts that are used regularly:

kappaLayout = #(define-event-function (parser location layoutType layoutSize) 
	(string? string?)
	(cond ((string-ci=? layoutType "solo") 
    	#{ 
    		%;#(set! paper-alist (cons '("10x13" . (cons (* 10 in) (* 13 in))) paper-alist))
    		\paper {
				#(set-paper-size "letter")
				left-margin = 18\mm
				%;system-separator-markup = \slashSeparator
				system-system-spacing #'basic-distance = #12
				system-system-spacing #'padding = #9
			}
    	#}
		)
	((string-ci=? layoutType "chamber") 
		#{ #}
		)
    (else 
    	#{ 
    		%error handling here??
    	#}
    	)
    )  
)



kappaLayoutTest = #(define-scheme-function (parser location layoutType layoutSize) 
	(string? string?)
	(cond ((string-ci=? layoutType "solo") 
    	#{ 
    		%;#(set! paper-alist (cons '("10x13" . (cons (* 10 in) (* 13 in))) paper-alist))
    	#}
		)
	((string-ci=? layoutType "chamber") 
		#{ #}
		)
    (else 
    	#{ 
    		%error handling here??
    	#}
    	)
    )  
)

