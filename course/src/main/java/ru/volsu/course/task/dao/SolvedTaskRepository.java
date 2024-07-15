package ru.volsu.course.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.volsu.course.task.model.SolvedTask;

@Repository
public interface SolvedTaskRepository extends JpaRepository<SolvedTask, Long> {
}
