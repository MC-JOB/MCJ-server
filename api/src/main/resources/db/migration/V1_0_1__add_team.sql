create table team
(
    id            bigint not null auto_increment,
    average_point double precision,
    created_at    datetime(6),
    description   varchar(255),
    logo          varchar(255),
    member_num    bigint,
    name          varchar(255),
    status        varchar(255),
    user_id       bigint,
    primary key (id)
);

alter table team
    add constraint FK7ab8b9m84sk9vvpmrn6b6tpjk foreign key (user_id) references user (id);