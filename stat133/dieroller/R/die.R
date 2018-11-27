die <- function(sides = c(1, 2, 3, 4, 5, 6), prob = c(rep((1/6), 6))) {
  check_sides(sides)
  check_prob(prob)

  object <- list(
    sides = sides,
    prob = prob)
  class(object) <- "die"
  object
}

check_sides <- function(sides) {
  if (length(sides) != 6) {
    stop("\n'prob' must be a vector of length 6")
  }
  if (!is.numeric(sides) & !is.character(sides)) {
    stop("\n'sides' must be a character or numeric vector")
  }
  TRUE
}

check_prob <- function(prob) {
  if (length(prob) != 6 | !is.numeric(prob)) {
    stop("\n'prob' must be a numeric vector of length 6")
  }
  if (any(is.na(prob))) {
    stop("\n'prob' cannot contain missing values")
  }
  if (any(prob < 0) | any(prob > 1)) {
    stop("\n'prob' values must be between 0 and 1")
  }
  if (sum(prob) != 1) {
    stop("\nelements in 'prob' must add up to 1")
  }
  TRUE
}

print.die <- function(x) {
  cat('object "die"\n\n')
  cd <- data.frame(
    side = x$sides, prob = x$prob
  )
  print(cd)
  invisible(x)
}

roll = function(die, times=1) {
  check_times(times)
  x = sample(die$sides, size = times, replace = TRUE, prob = die$prob)
  object = list(rolls = x,
                sides = die$sides,
                prob = die$prob,
                total = times)
  class(object) = "roll"
  object
}

check_times = function(x) {
  if (x <= 0 | !is.numeric(x)) {
    stop("\nargument 'times' must be a positive integer")
  } else {
    TRUE
  }
}

print.roll = function(x) {
  cat('object "roll"\n\n')
  print(list(rolls = x$rolls))
  invisible(x)
}

a = roll(die(), 50)

summary.roll = function(x) {
  proportions = c(
    sum(x$rolls == x$sides[1]) / x$total,
    sum(x$rolls == x$sides[2]) / x$total,
    sum(x$rolls == x$sides[3]) / x$total,
    sum(x$rolls == x$sides[4]) / x$total,
    sum(x$rolls == x$sides[5]) / x$total,
    sum(x$rolls == x$sides[1]) / x$total
  )
  freqs = data.frame(
    side = x$sides,
    count = c(
      sum(x$rolls == x$sides[1]),
      sum(x$rolls == x$sides[2]),
      sum(x$rolls == x$sides[3]),
      sum(x$rolls == x$sides[4]),
      sum(x$rolls == x$sides[5]),
      sum(x$rolls == x$sides[6])
    ),
    prop = proportions
  )
  object = list(freqs = freqs)
  class(object) = "summary.roll"
  object
}

print.summary.roll = function(x) {
  cat('summary "roll"\n\n')
  print(x$freqs)
  invisible(x)
}

plot.roll = function(x) {
  frequencies = c(
    sum(x$rolls == x$sides[1]) / x$total,
    sum(x$rolls == x$sides[2]) / x$total,
    sum(x$rolls == x$sides[3]) / x$total,
    sum(x$rolls == x$sides[4]) / x$total,
    sum(x$rolls == x$sides[5]) / x$total,
    sum(x$rolls == x$sides[1]) / x$total)
  barplot(frequencies, names.arg = x$sides, xlab = "sides of die",
          ylab = "relative frequencies",
          main = paste0("Frequencies in a series of ", x$total, " die rolls"))
}

"[.roll" <- function(x, i) {
  x$rolls[i]
}

"[<-.roll" <- function(x, i, value) {
  if (value != x$sides[1] & value != x$sides[2] & value != x$sides[3]
      & value != x$sides[4] & value != x$sides[5] & value != x$sides[6]) {
    stop(sprintf('\nreplacing value must be %s, %s, %s, %s, %s, or %s',
                 x$sides[1], x$sides[2], x$sides[3], x$sides[4], x$sides[5], x$sides[6]))
  }
  if (i > x$total) {
    stop("\nindex out of bounds")
  }
  x$rolls[i] <- value
  x
}

make_roll = function(die, rolls) {
  rol = list(
    rolls = rolls,
    sides = die$sides,
    prob = die$prob,
    total = length(rolls)
  )
  class(rol) = "roll"
  rol
}

"+.roll" <- function(obj, incr) {
  if (length(incr) != 1 | incr <= 0) {
    stop("\ninvalid increment")
  }
  more_rolls <- roll(obj, times = incr)$rolls
  make_roll(obj, c(obj$rolls, more_rolls))
}
