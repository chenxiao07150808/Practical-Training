create database if not exists CPYD;
use CPYD;

CREATE TABLE ticketlist
(
   id           BIGINT  not null auto_increment,
   shuttle_id   BIGINT not null,
   user_id       BIGINT not null,
   status       VARCHAR(20),
   
   PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

insert into ticketlist(shuttle_id, user_id, status) values (1, 10001, '已预订');
insert into ticketlist(shuttle_id, user_id, status) values (2, 10001, '已预订');

