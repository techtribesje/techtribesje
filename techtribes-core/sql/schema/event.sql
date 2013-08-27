DROP TABLE IF EXISTS event;

CREATE TABLE event (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(256) character set utf8 not null,
    description varchar(10240) character set utf8 not null,
    island char(1) not null,
    content_source_id int,
    url varchar(256),
    start_datetime DATETIME not null,
    end_datetime DATETIME,
    location varchar(128) character set utf8,
    primary key (id));