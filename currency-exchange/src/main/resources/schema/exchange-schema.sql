drop table if exists exchange CASCADE;
drop sequence if exists hibernate_sequence;
create table exchange (
                          id bigint not null,
                          conversion_multiple decimal(100,50) not null,
                          currency_from varchar(255) not null,
                          currency_to varchar(255) not null,
                          exchange_environment_info varchar(255),
                          primary key (id)
);

