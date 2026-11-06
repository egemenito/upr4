package com.example.todo_app.repository;

import com.example.todo_app.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/** Съхранение на задачите в паметта. */
@Repository
public class TaskRepository {

    private final Map<Long, Task> tasks = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public synchronized List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public synchronized Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public synchronized Task add(Task task) {
        task.setId(sequence.incrementAndGet());
        tasks.put(task.getId(), task);
        return task;
    }

    public synchronized Task update(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public synchronized void deleteById(Long id) {
        tasks.remove(id);
    }
}
