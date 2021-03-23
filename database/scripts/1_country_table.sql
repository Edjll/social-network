create table country
(
    id    integer not null
        constraint country_pkey
            primary key,
    title varchar(100) default NULL::character varying
);