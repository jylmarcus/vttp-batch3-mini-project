USE railway;

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