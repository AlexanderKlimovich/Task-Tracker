package com.klymovych.tasktracker.controller;

import com.klymovych.tasktracker.dto.TaskDto;
import com.klymovych.tasktracker.dto.TaskTransformer;
import com.klymovych.tasktracker.model.Priority;
import com.klymovych.tasktracker.model.Task;
import com.klymovych.tasktracker.service.StateService;
import com.klymovych.tasktracker.service.TaskService;
import com.klymovych.tasktracker.service.ToDoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;

    public TaskController(TaskService taskService, ToDoService todoService, StateService stateService) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.stateService = stateService;
    }

    @GetMapping("/create/todos/{todo_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String create(@PathVariable("todo_id") long todoId, Model model) {
        model.addAttribute("task", new TaskDto());
        model.addAttribute("todo", todoService.readById(todoId));
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDto taskDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("todo", todoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            return "create-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.getByName("New")
        );
        taskService.create(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model) {
        TaskDto taskDto = TaskTransformer.convertToDto(taskService.readById(taskId));
        model.addAttribute("task", taskDto);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", stateService.getAll());
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task")TaskDto taskDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", stateService.getAll());
            return "update-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.readById(taskDto.getStateId())
        );
        taskService.update(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String delete(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId) {
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }
}

