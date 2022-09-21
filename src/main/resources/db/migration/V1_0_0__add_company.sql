CREATE TABLE company
(
    id          int         NOT NULL AUTO_INCREMENT,
    email       varchar(45) NOT NULL,
    password    varchar(45) NOT NULL,
    name        varchar(7)  NOT NULL,
    description varchar(45) DEFAULT NULL,
    logo        varchar(45) DEFAULT NULL,
    createAt    datetime    NOT NULL,
    PRIMARY KEY (id)
);
