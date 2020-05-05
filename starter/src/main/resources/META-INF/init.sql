create database if not exists msg;

create table msg.msg_content
(
    msg_id varchar(256) not null
        primary key,
    msg_key varchar(256) not null ,
    msg_desc varchar(4096) nullable ,
    msg_content blob not null ,
    msg_properties varchar(4096) nullable ,
    constraint msg_key
        unique (msg_key)
);

create table msg.msg_lifecycle
(
    id int primary key,
    msg_id varchar(256) not null ,
    msg_key varchar(256) not null ,
    phase varchar(16) not null ,
    phase_status varchar(16) not null default 'start',
    host varchar(1024) not null ,
    port int not null ,
    constraint msg_lifecycle_msg_content_msg_id_fk
        foreign key (msg_id) references msg.msg_content (msg_id)
);

create table msg.meta_data
(
	id varchar(256) not null
		primary key,
	`desc` varchar(256) not null,
	virtual_host varchar(256) not null,
	tag varchar(256) not null,
	properties varchar(4096) null
);



create table msg.log_info(
    id int primary key;
    create_time date not null ,
    layer varchar(128) not null ,
    type varchar(128) not null ,
    trace_id varchar(256),
    result varchar(1024),
    content varchar(4096)
)


