create table if not exists articles
(
    article_id
    serial
    primary
    key,
    "text"
    text,
    title
    varchar
(
    255
),
    tag varchar
(
    255
)
    );