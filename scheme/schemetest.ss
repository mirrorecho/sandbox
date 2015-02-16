(let ((x '((a b) c')))
	(cons 
		(let ((x (cdr x)))
			(car x))
		(let ((x (car x)))
			(cons 
				(let ((x (cdr x)))
					(car x))
				(cons 
					(let ((x (car x)))
						x)
					(cdr x)
				)
			)
		)
	)
)




(let ((x '((a b) c')))
	(cons 
		(let ((x2 (cdr x)))
			(car x2))
		(let ((x3 (car x)))
			(cons 
				(let ((x4 (cdr x3)))
					(car x4))
				(cons 
					(let ((x5 (car x3)))
						x5)
					(cdr x3)
				)
			)
		)
	)
)