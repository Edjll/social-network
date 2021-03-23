create table city
(
    id         integer not null
        constraint city_pkey
            primary key,
    country_id integer
        constraint city_country_id_fk
            references country,
    title      varchar(100) default NULL::character varying
);