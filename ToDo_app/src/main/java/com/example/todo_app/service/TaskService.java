package com.example.todo_app.service;

import com.example.todo_app.model.Task;
import com.example.todo_app.model.TaskForm;
import com.example.todo_app.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Задачата не е намерена."));
    }

    public Task addTask(TaskForm form) {
        Task task = new Task(form.getTitle().trim(), clean(form.getDescription()));
        return taskRepository.add(task);
    }

    public Task editTask(Long id, TaskForm form) {
        Task task = getTask(id);
        task.setTitle(form.getTitle().trim());
        task.setDescription(clean(form.getDescription()));
        return taskRepository.update(task);
    }

    public Task toggleCompleted(Long id) {
        Task task = getTask(id);
        task.setCompleted(!task.isCompleted());
        return taskRepository.update(task);
    }

    public void deleteTask(Long id) {
        getTask(id);
        taskRepository.deleteById(id);
    }

    private String clean(String text) {
        return text == null ? "" : text.trim();
    }
}
