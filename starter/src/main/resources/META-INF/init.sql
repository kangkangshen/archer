create database if not exists msg;

create table msg.msg_content
(
    msg_id varchar(256) not null
        primary key,
    msg_key varchar(256) null,
    msg_desc varchar(4096) null,
    msg_content blob null,
    msg_properties varchar(4096) null,
    constraint msg_key
        unique (msg_key)
);

create table msg.msg_lifecycle
(
    id int not null
        primary key,
    msg_id varchar(256) null,
    msg_key varchar(256) null,
    phase varchar(16) null,
    phase_status varchar(16) null,
    host varchar(1024) null,
    port int null,
    constraint msg_lifecycle_msg_content_msg_id_fk
        foreign key (msg_id) references msg.msg_content (msg_id)
);



create database if not exists metadata;