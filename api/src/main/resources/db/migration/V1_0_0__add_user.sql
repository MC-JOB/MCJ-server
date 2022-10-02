create table user
(
    id         bigint not null auto_increment,
    age        bigint,
    created_at datetime(6),
    email      varchar(255),
    interest   varchar(255),
    nickname   varchar(255),
    password   varchar(255),
    status     varchar(255),
    primary key (id)
);

alter table user add constraint uq_email unique (email);
