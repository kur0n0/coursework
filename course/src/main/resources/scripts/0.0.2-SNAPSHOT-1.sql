create table task(
                     task_id serial primary key,
                     question varchar(500),
                     answer varchar(500),
                     hint_article_id int4,
                     answer_mapping varchar(50)
);

create table taskhistory(
                    task_history_id serial primary key,
                    created timestamp,
                    username varchar(255),
                    user_answer varchar(255),
                    task_id int8 references task(task_id)
);

create table solvedtask(
                    solved_task_id serial primary key,
                    created timestamp,
                    username varchar(255),
                    task_id int8 references task(task_id)
);