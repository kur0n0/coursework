create table if not exists users(
    userid serial primary key,
    name varchar(255),
    surname varchar(255),
    patronymic varchar(255),
    username varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) not null
);

