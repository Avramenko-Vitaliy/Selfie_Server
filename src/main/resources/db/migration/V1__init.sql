CREATE TABLE user
(
id BIGINT(20) PRIMARY KEY NOT NULL,
account_expired BIT(1) NOT NULL,
credentials_expired BIT(1) NOT NULL,
email VARCHAR(255),
enabled BIT(1) NOT NULL,
locked BIT(1) NOT NULL,
password VARCHAR(255),
username VARCHAR(255)
);

CREATE TABLE user_role
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  authority VARCHAR(255)
);

CREATE TABLE selfie
(
id BIGINT(20) PRIMARY KEY NOT NULL,
date BIGINT(20) NOT NULL,
description VARCHAR(255),
title VARCHAR(255),
id_user BIGINT(20) NOT NULL,
CONSTRAINT FK_SELFIE_TO_USER FOREIGN KEY (id_user) REFERENCES user (id)
);

CREATE TABLE authority
(
  user_id BIGINT(20) NOT NULL,
  authorities_id BIGINT(20) NOT NULL,
  CONSTRAINT FK_AUTH_TO_USER FOREIGN KEY (user_id) REFERENCES user (id),
  CONSTRAINT FK_AUTH_TO_ROLE FOREIGN KEY (authorities_id) REFERENCES user_role (id)
);

CREATE INDEX FK_AUTH_TO_USER ON authority (user_id);
CREATE INDEX FK_AUTH_TO_ROLE ON authority (authorities_id);
CREATE UNIQUE INDEX UK_USERNAME ON user (username);
CREATE INDEX FK_SELFIE_TO_USER ON selfie (id_user);

INSERT INTO user_role(id,authority)VALUES(1,'user');
