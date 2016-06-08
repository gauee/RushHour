drop table result;
drop table result_detail;

create table result(
id        char(128)   primary key,
author    char(64)    not null,
lang      char(8)     not null,
creation  text        not null
);

create table result_detail(
result_id     char(128) not null,
test_case_id  char(16)  not null,
moves         char(128)   not null,
duration      INTEGER   not null,
FOREIGN Key(result_id) REFERENCES result(id)
);

alter table result_detail add column msg {char(128) default "executed"};
alter table result add column msg {char(128) default ""};
alter table result_detail add column car_moves {char(128) default "-1,-1"};
