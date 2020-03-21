CREATE DATABASE local;
CREATE DATABASE test;
CREATE USER 'mysqluser'@'%' IDENTIFIED BY 'mysqluser';
GRANT ALL ON *.* to 'mysqluser'@'%' WITH GRANT OPTION;
