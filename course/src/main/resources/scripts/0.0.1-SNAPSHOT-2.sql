create table if not exists users
(
    user_id
    serial
    primary
    key,
    name
    varchar
(
    255
),
    surname varchar
(
    255
),
    patronymic varchar
(
    255
),
    username varchar
(
    255
) not null,
    password varchar
(
    255
) not null,
    role varchar
(
    255
) not null
    );

insert into users(name, surname, username, password, role)
values ('Владислав', 'Ким', 'admin', '$2a$10$rmSZgvX8b5IRheDdXX6VKuzy4j.ihgccgI/AzTph5vC7R..z4nZU.', 'admin');
