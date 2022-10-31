create table team_logo
(
    id         bigint not null auto_increment,
    name       varchar(255),
    saved_name varchar(255),
    size       bigint,
    team_id    bigint,
    primary key (id)
);

alter table team_logo
    add constraint FK79x4omeny2rl9253xnnhc4ber foreign key (team_id) references team (id);
