package ru.volsu.course.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.volsu.course.model.SolvedTask;

@Repository
public interface SolvedTaskRepository extends JpaRepository<SolvedTask, Long> {
}
