# -----------------------------------------------------------------------------
# Title: Binomial Functions
# Description: This script creates several functions useful for creating the
#                 binomial probability function.
# Author(s): Ian Driscoll
# Date: 3-23-2018
# -----------------------------------------------------------------------------

#' @title is integer
#' @description checks if x is an integer
#' @param x is any number
#' @return TRUE or FALSE
is_integer = function(x) {
  return(x %% 1 == 0)
}

#' @title is positive
#' @description check if x is positive
#' @param x is any number
#' @return TRUE or FALSE
is_positive = function(x) {
  return(x > 0)
}

#' @title is nonnegative
#' @description check if x is nonnegative
#' @param x is any number
#' @return TRUE or FALSE
is_nonnegative = function(x) {
  return(x >= 0)
}

#' @title is positive integer
#' @description check if x is positive integer
#' @param x is any number
#' @return TRUE or FALSE
is_positive_integer = function(x) {
  return(x > 0 & is_integer(x))
}

#' @title is nonneg integer
#' @description check if x is nonneg integer
#' @param x is any number
#' @return TRUE or FALSE
is_nonneg_integer = function(x) {
  return(x >= 0 & is_integer(x))
}

#' @title is pprobability
#' @description check if x is a valid probability
#' @param x is any number
#' @return TRUE or FALSE
is_probability = function(x) {
  return(x >= 0 & x <= 1)
}

#' @title bin factorial
#' @description finds the factorial of a nonnegative integer
#' @param x is any number
#' @return factorial of x
bin_factorial = function(x) {
  result = 1
  if (x == 0) {
    return(result)
  }
  for (i in 1:x) {
    result = result * i
  }
  return(result)
}

#' @title bin combinations
#' @description finds binomial combinations of numbers n choosing k
#' @param n is number of options
#' @param k is number of choices
#' @return number of combinations
bin_combinations = function(n, k) {
  return((bin_factorial(n)) / (bin_factorial(k) * bin_factorial(n - k)))
}

#' @title bin probability
#' @description finds probability in a binomial distributions
#' @param trials is number of trials
#' @param success is number of successes
#' @param prob is probability of success
#' @return probability of # successes in # trials
bin_probability = function(trials, success, prob) {
  if (!is_nonneg_integer(trials) | !is_nonneg_integer(success)) {
    stop("Trials and successes must be nonnegative integers")
  }
  else if (!is_probability(prob)) {
    stop("Probability must be valid probability")
  }
  return(bin_combinations(trials, success) * (prob^(success)) * ((1 - prob)^(trials - success)))
}

#' @title bin distribution
#' @description finds distribution of a binomial
#' @param trials is number of trials
#' @param prob is probability of success
#' @return data frame of distribution
bin_distribution = function(trials, prob) {
  if (!is_nonneg_integer(trials)) {
    stop("Trials must be a nonnegative integer")
  }
  else if (!is_probability(prob)) {
    stop("Probability must be valid probability")
  }
  success = 0:trials
  probability = rep(0, trials + 1)
  for (i in success) {
    probability[i + 1] = bin_probability(trials, i, prob)
  }
  return(data.frame(success, probability))
}
bin_distribution(5, 0.5)



