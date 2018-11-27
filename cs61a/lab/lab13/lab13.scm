; Q1
(define (compose-all funcs)
  (define (end funs value)
  (if (null? funs)
    value
    (end (cdr funs) ((car funs) value))))
  (define (final a)
  (end funcs a))
  final
)

; Q2
(define (tail-replicate x n)
  (define (repl num times lst)
  (if (= 0 times)
  lst
  (repl num (- times 1) (cons num lst))))
(repl x n nil)
)
