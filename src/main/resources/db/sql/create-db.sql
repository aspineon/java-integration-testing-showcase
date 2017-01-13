CREATE TABLE person (
  personId bigint(20) auto_increment PRIMARY KEY,
  email varchar(255) DEFAULT NULL,
  firstName varchar(255) NOT NULL,
  lastName varchar(255) NOT NULL,
  ssn varchar(255) NOT NULL,
  UNIQUE KEY UK_ssn (ssn),
  UNIQUE KEY UK_email (email)
);
