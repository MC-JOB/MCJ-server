create table resume
(
    id               bigint not null auto_increment,
    content          varchar(255),
    created_at       datetime(6),
    status           varchar(255),
    title            varchar(255),
    training_history varchar(255),
    user_id          bigint,
    primary key (id)
);

alter table resume
    add constraint FKiqntisdlc7ta7sjr6d8rj5ae2 foreign key (user_id) references user (id);
