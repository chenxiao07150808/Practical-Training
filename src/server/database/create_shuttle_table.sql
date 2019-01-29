create database if not exists CPYD;

use CPYD;
CREATE TABLE shuttlelist
(
  id        BIGINT  not null auto_increment,
  s_starting  VARCHAR(50) not null,
  s_ending    VARCHAR(30) not null,
  s_date      DATE,
  s_time      TIME,
  capacity    smallint default 50,
  seating     smallint default 50  check(seating >= 0),
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('中大', '南方学院', '2014-08-08', '10:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('南方学院', '中大', '2014-08-08', '12:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('中大', '南方学院', '2014-08-09', '10:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('南方学院', '中大', '2014-08-09', '12:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('中大', '南方学院', '2014-08-010', '10:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('南方学院', '中大', '2014-08-010', '12:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('中大', '南方学院', '2014-08-011', '10:30:00', 50, 50);
insert into shuttlelist(s_starting, s_ending, s_date, s_time, capacity, seating) values ('南方学院', '中大', '2014-08-011', '12:30:00', 50, 50);

