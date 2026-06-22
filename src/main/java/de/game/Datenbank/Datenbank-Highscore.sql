create database Spaceinvaders;

use Spaceinvaders;

create table highscore (
	id int auto_increment primary key, 
    player varchar(30), 
    score int, 
    quote double,
    level int,
    created_at timestamp default current_timestamp);

CREATE USER 'spieler'@'%' IDENTIFIED BY 'Carlotta19!';

GRANT ALL PRIVILEGES ON *.* TO 'spieler'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;
