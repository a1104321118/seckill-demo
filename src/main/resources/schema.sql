CREATE DATABASE seckill;

use seckill;

CREATE TABLE seckill(
seckill_id bigint not null AUTO_INCREMENT,
name varchar(120) not null,
number int not null,
start_time datetime not null,
end_time datetime not null,
create_time datetime not null,
primary key (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

insert into seckill(name,number,start_time,end_time,create_time) 
values
('1000元秒杀iphone7',100,'2016-09-11 00:00:00','2016-09-11 23:59:59','2016-09-11 23:59:59'),
('100元秒杀iphone7',100,'2016-09-11 00:00:00','2016-09-11 23:59:59','2016-09-11 23:59:59'),
('10元秒杀iphone7',100,'2016-09-11 00:00:00','2016-09-11 23:59:59','2016-09-11 23:59:59'),
('1元秒杀iphone7',100,'2016-09-11 00:00:00','2016-09-11 23:59:59','2016-09-11 23:59:59');

CREATE TABLE success_seckill(
seckill_id bigint not null,
user_phone bigint not null,
state tinyint not null default -1,
create_time timestamp not null,
primary key(seckill_id,user_phone),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8

mysql -uroot -proot