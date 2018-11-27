
context("Check die arguments")

test_that("check_sides with ok vectors", {

  expect_true(check_sides(c('heads', 'tails', 'a', 'b', 'c', 'd')))
  expect_true(check_sides(c(1, 2, 3, 4, 5, 6)))
})


test_that("check_sides fails with invalid lengths", {

  expect_error(check_sides(c('one', 'two', 'three')))
  expect_error(check_sides(c('one')))
  expect_error(check_sides(1:5))
  expect_error(check_sides(1))
})

test_that("check_prob works with ok vectors", {

  expect_true(check_prob(c(0.1, 0.2, 0.3, 0.2, 0.1, 0.1)))
  expect_true(check_prob(c(0, 0, 0, 0, 0, 1)))
})


test_that("check_prob fails with invalid lengths", {

  expect_error(check_prob(1:5))
  expect_error(check_prob(1))
})


test_that("check_prob fails with invalid numbers", {

  expect_error(check_prob(0.333, 0.666, 1, 2, 4, 5))
  expect_error(check_prob(-0.5, 0.5, 0.1, 0.2, 0.3, 0.4))
})

test_that("die() works with ok args", {
  expect_equal(class(die()), "die")
  expect_equal(length(die()$sides), 6)
})

test_that("die() errors with invalid args", {
  expect_error(die(c(1, 2, 3), c(0.4, 0.2, 0.4)))
  expect_error(die(5))
})

test_that("roll() works with ok args", {
  expect_equal(class(roll(die())), "roll")
  expect_equal(length(roll(die())$rolls), 1)
  expect_equal(roll(die(), 500)$total, 500)
})

test_that("roll() errors with invalid args", {
  expect_error(roll())
  expect_error(roll(die(), 5, 2))
  expect_error(roll(die(), -1))
})
