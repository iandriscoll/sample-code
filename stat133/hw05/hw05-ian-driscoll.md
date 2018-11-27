hw05-ian-driscoll
================

De Mere's Problem
-----------------

### Problem 1.

``` r
mydie = die()
successes = c(rep(0, 1000))
for (i in 1:1000) {
  a = roll(mydie, times = 4)
  if (length(a$rolls[a$rolls == 6]) > 0) {
    successes[i] = 1
  }
}

wins = sum(successes)
rel_freq = wins / 1000
rel_freq
```

    ## [1] 0.518

### Problem 2.

``` r
mydie2 = die()
successes2 = c(rep(0, 1000))
for (i in 1:1000) {
  x = roll(mydie, 24)
  y = roll(mydie, 24)
  for (k in 1:24) {
    if (x$rolls[k] == y$rolls[k] & x$rolls[k] == 6) {
    successes2[i] = 1
    }
  }
}

wins2 = sum(successes2)
rel_freq2 = wins2 / 1000
rel_freq2
```

    ## [1] 0.507