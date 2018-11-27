# 2.1

library(dplyr)
library(stringr)

split_chars = function(x) {
  a = str_split(x, pattern = '')
  return(unlist(a))
}
split_chars('Go Bears!')

# 2.2

num_vowels = function(x) {
  vowels <- c('a', 'e', 'i', 'o', 'u')
  x = c(x, vowels)
  x = x[x %in% vowels]
  table(x) - 1
}
num_vowels(split_chars("Go Bears!"))

# 2.3

count_vowels = function(x) {
  a = split_chars((tolower(x)))
  return(num_vowels(a))
}

count_vowels("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG")

reverse_chars = function(x) {
  a = split_chars(x)
  a = rev(a)
  x = a[1]
  for (i in 2:length(a)) {
    x = paste0(x, a[i])
  }
  return(x)
}
reverse_chars("Lumox Maxima")

reverse_words = function(x) {
  a = unlist(str_split(x, pattern = ' '))
  a = rev(a)
  x = a[1]
  if (length(a) == 1) {
    return(x)
  }
  for (i in 2:length(a)) {
    x = paste(x, a[i], sep = " ")
  }
  return(x)
}
reverse_words("sentence! this reverse")
reverse_words("string")
