create table recruitment_process
(
    id             bigint not null auto_increment,
    closed_at      datetime(6),
    created_at     datetime(6),
    status         varchar(255),
    recruitment_id bigint,
    user_id        bigint,
    primary key (id)
);

alter table recruitment_process
    add constraint uq_recruitment_user unique (recruitment_id, user_id);

alter table recruitment_process
    add constraint FK52wdp6yo4y20195jhxjf7w1y6 foreign key (recruitment_id) references recruitment (id);

alter table recruitment_process
    add constraint FKe3kksm4w4x4dsilvd6cndkk03 foreign key (user_id) references user (id);
