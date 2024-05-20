package ru.volsu.course.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solvedtask")
public class SolvedTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solved_task_id")
    private Long solvedTaskId;

    private LocalDateTime created;

    @Column(name = "username")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public void setSolvedTaskId(Long solvedTaskId) {
        this.solvedTaskId = solvedTaskId;
    }

    public Long getSolvedTaskId() {
        return solvedTaskId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
