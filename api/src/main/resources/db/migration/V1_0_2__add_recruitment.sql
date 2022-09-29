create table recruitment
(
    id         bigint not null auto_increment,
    closed_at  datetime(6),
    content    varchar(255),
    created_at datetime(6),
    status     varchar(255),
    title      varchar(255),
    team_id    bigint,
    primary key (id)
);

alter table recruitment
    add constraint FK7ppvmsval12q2ej837rn2mkcc foreign key (team_id) references team (id);
