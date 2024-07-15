package ru.volsu.course.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.volsu.course.task.model.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true, value = "select t.* from Task t where t.task_id not in (select st.task_id from SolvedTask st where st.username = :username) " +
            "order by random() " +
            "limit 1")
    Optional<Task> findRandomNotSolvedTask(@Param("username") String username);

}
