CREATE DATABASE miniprojDB;

USE miniprojDB;

CREATE TABLE app_user (
	user_id varchar(255) NOT NULL,
    username varchar(255) NOT NULL,
    enc_password varchar(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE saved_routes(
	user_id varchar(255) NOT NULL,
    RouteRequestId varchar(255) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_user(user_id)
);

select * from app_user where username = 'fred';

select * from saved_routes;

delete from saved_routes where user_id = "4ec3d2e5";