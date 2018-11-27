CREATE TABLE parents AS
  SELECT "abraham" AS parent, "barack" AS child UNION
  SELECT "abraham"          , "clinton"         UNION
  SELECT "delano"           , "herbert"         UNION
  SELECT "fillmore"         , "abraham"         UNION
  SELECT "fillmore"         , "delano"          UNION
  SELECT "fillmore"         , "grover"          UNION
  SELECT "eisenhower"       , "fillmore";

CREATE TABLE dogs AS
  SELECT "abraham" AS name, "long" AS fur, 26 AS height UNION
  SELECT "barack"         , "short"      , 52           UNION
  SELECT "clinton"        , "long"       , 47           UNION
  SELECT "delano"         , "long"       , 46           UNION
  SELECT "eisenhower"     , "short"      , 35           UNION
  SELECT "fillmore"       , "curly"      , 32           UNION
  SELECT "grover"         , "short"      , 28           UNION
  SELECT "herbert"        , "curly"      , 31;

CREATE TABLE sizes AS
  SELECT "toy" AS size, 24 AS min, 28 AS max UNION
  SELECT "mini"       , 28       , 35        UNION
  SELECT "medium"     , 35       , 45        UNION
  SELECT "standard"   , 45       , 60;

-------------------------------------------------------------
-- PLEASE DO NOT CHANGE ANY SQL STATEMENTS ABOVE THIS LINE --
-------------------------------------------------------------

-- The size of each dog
CREATE TABLE size_of_dogs AS
  SELECT a.name, b.size FROM dogs AS a, sizes AS b
    WHERE a.height > b.min AND a.height <= b.max;

-- All dogs with parents ordered by decreasing height of their parent
CREATE TABLE by_height AS
  SELECT b.child FROM dogs AS a, parents AS b
    WHERE a.name = b.parent ORDER BY -a.height;

-- Filling out this helper table is optional
CREATE TABLE siblings AS
  SELECT 'Your code';

-- Sentences about siblings that are the same size
CREATE TABLE sentences AS
  SELECT b.name || " and " || a.name || " are " || b.size || " siblings"
    FROM size_of_dogs AS a, size_of_dogs AS b, parents AS c, parents AS d
      WHERE a.size = b.size AND a.name != b.name AND a.name = c.child
      AND b.name = d.child AND c.parent = d.parent AND a.name > b.name;

-- Ways to stack 4 dogs to a height of at least 170, ordered by total height
CREATE TABLE stacks_helper(dogs, stack_height, last_height);
-- Add your INSERT INTOs here




CREATE TABLE stacks AS
  WITH
    stack_myhelp(dogs, total, n, max) AS (
      SELECT name, height, 1, height FROM dogs UNION
      SELECT dogs || ', ' || name, total+height, n+1, height
        FROM stack_myhelp, dogs
          WHERE n < 4 AND max < height)
  SELECT dogs, total FROM stack_myhelp
    WHERE total >= 170 AND n = 4 ORDER BY total;
