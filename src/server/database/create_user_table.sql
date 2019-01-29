create database if not exists CPYD;
use CPYD;

CREATE TABLE userlist
(
   id        BIGINT  not null,
   name      VARCHAR(50) not null,
   stuNum    VARCHAR(30) not null,
   pwd       VARCHAR(20),
   
   PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

insert into userlist values (0, 'admin', 'admin', 'admin');
insert into userlist values (10001, '10001', '10001', '10001');
insert into userlist values (10002, '10002', '10002', '10002');
insert into userlist values (10003, '10003', '10003', '10003');
