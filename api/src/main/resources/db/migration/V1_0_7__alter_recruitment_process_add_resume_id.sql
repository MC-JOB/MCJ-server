alter table recruitment_process
    add column resume_id bigint;

alter table recruitment_process
    add constraint FKeqr2kv62kwg0vl0o88jsx9nsc foreign key (resume_id) references resume (id);
