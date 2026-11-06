package com.example.todo_app.controller;

import com.example.todo_app.model.Task;
import com.example.todo_app.model.TaskForm;
import com.example.todo_app.service.StatsService;
import com.example.todo_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final StatsService statsService;

    public TaskController(TaskService taskService, StatsService statsService) {
        this.taskService = taskService;
        this.statsService = statsService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/tasks";
    }


    @GetMapping("/tasks")
    public String list(Model model) {
        model.addAttribute("taskForm", new TaskForm());
        fillModel(model, null);
        return "tasks";
    }

    @PostMapping("/tasks")
    public String add(@Valid TaskForm taskForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            fillModel(model, null);
            return "tasks";
        }
        taskService.addTask(taskForm);
        return "redirect:/tasks";
    }


    @GetMapping("/tasks/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTask(id);
        model.addAttribute("taskForm", new TaskForm(task.getTitle(), task.getDescription()));
        fillModel(model, id);
        return "tasks";
    }

    @PostMapping("/tasks/{id}/edit")
    public String edit(@PathVariable Long id, @Valid TaskForm taskForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            fillModel(model, id);
            return "tasks";
        }
        taskService.editTask(id, taskForm);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        taskService.toggleCompleted(id);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    private void fillModel(Model model, Long editId) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("total", statsService.getTotalCount());
        model.addAttribute("completedCount", statsService.getCompletedCount());
        model.addAttribute("activeCount", statsService.getActiveCount());
        model.addAttribute("editId", editId);
    }
}
