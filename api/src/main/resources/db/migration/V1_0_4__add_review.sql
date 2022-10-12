create table review
(
    id         bigint not null auto_increment,
    content    varchar(255),
    created_at datetime(6),
    score      bigint,
    status     varchar(255),
    team_id    bigint,
    user_id    bigint,
    primary key (id)
);

alter table review
    add constraint uq_user_team unique (user_id, team_id);

alter table review
    add constraint FK4a245rd9e9a0gholo36rcau7a foreign key (team_id) references team (id);

alter table review
    add constraint FKiyf57dy48lyiftdrf7y87rnxi foreign key (user_id) references user (id);
