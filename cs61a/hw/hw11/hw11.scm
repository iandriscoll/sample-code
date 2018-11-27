(define (find s predicate)
  (cond
    ((null? s) #f)
    ((predicate (car s)) (car s))
    (else (find (cdr-stream s) predicate))
))

(define (scale-stream s k)
  (cons-stream (* (car s) k) (scale-stream (cdr-stream s) k))
)

(define (has-cycle s)
  (define (cycle-check s new-s)
    (cond
      ((null? new-s) #f)
      ((null? (cdr new-s)) #f)
      ((eq? s (cdr-stream new-s)) #t)
      (else (cycle-check (cdr-stream s) (cdr-stream (cdr-stream new-s))))
  ))
  (cycle-check s s))


(define (has-cycle-constant s)
  'YOUR-CODE-HERE
)
