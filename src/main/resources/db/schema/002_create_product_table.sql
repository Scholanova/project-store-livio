--liquibase formatted sql

--changeset scholanova:1
CREATE TABLE IF NOT EXISTS PRODUCT (
  ID                  SERIAL          NOT NULL,
  NAME                VARCHAR(255)    NOT NULL,
  QUANTITY			  INTEGER 		  NOT NULL,
  IDSTORE			  INTEGER		  NOT NULL,
  PRIMARY KEY (ID)
);
