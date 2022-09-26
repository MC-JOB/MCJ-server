create table team
(
    id            bigint not null auto_increment,
    average_point bigint,
    created_at    datetime(6),
    description   varchar(255),
    email         varchar(255),
    logo          varchar(255),
    member_num    bigint,
    name          varchar(255),
    password      varchar(255),
    status   varchar(255),
    primary key (id)
);