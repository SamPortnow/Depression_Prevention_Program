drop table if exists calendar;
create table calendar (
  id integer primary key autoincrement,
  username text, Year integer, Day integer, minutes integer,
  Activity text, Feeling integer, Thought text
);

drop table if exists games;
create table games (
  id integer primary key autoincrement,
  username string not null,
  Time long, RT long, Score int,
  GameNumber int, Game_Complete text, Trial int,
  negative_thought string, positive_thought string, Success string
);

