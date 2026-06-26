// Dieses SQL-Skript erstellt die benötigte Datenbank und Tabelle für das Spiel "Space Invaders".
// Es legt eine Highscore-Tabelle an, in der Spielername, Punkte, Trefferquote,
// Level und Zeitpunkt gespeichert werden.
// Zusätzlich wird ein Datenbankbenutzer erstellt und mit allen nötigen Rechten ausgestattet,
// damit das Spiel auf die Datenbank zugreifen und Highscores speichern kann.


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
