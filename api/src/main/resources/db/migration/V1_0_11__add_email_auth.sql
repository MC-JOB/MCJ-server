create table email_auth
(
    id        bigint  not null auto_increment,
    code      varchar(255),
    email     varchar(255),
    sent_at   datetime(6),
    try_count integer not null,
    primary key (id)
);

alter table email_auth
    add constraint uq_email_auth_email unique (email);
