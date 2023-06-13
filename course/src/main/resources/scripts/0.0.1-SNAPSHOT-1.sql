create table if not exists article(
    articleid serial primary key,
    "text" text,
    title varchar(255),
    tag varchar(255)
);