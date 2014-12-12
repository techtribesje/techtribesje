DROP TABLE IF EXISTS github_repo;

CREATE TABLE github_repo (
    content_source_id int not null,
    name varchar(128) character set utf8 not null,
	  description varchar(10240) character set utf8 null,
    url varchar(1024) character set utf8 not null,
    fork bit not null default 0,
    primary key (content_source_id, name));