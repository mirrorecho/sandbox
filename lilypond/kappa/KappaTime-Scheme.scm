
(define kappaIsMetered #t)

(define kappaSetMetered 
	(lambda (isMetered)
		(set! kappaIsMetered isMetered)
	)
)

(define kappaGetMetered
	(lambda ()
	kappaIsMetered
	)
)