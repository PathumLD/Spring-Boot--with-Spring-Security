package com.example.todo_management.controller;

import com.example.todo_management.dto.TodoDTO;
import com.example.todo_management.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/todos")
public class TodoController {

    private TodoService todoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("addTodo")
    public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoDTO todoDTO) {
        TodoDTO savedTodo = todoService.addTodo(todoDTO);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    //add get todo
    @PreAuthorize("permitAll()")
    @GetMapping("getTodo/{id}")
    public ResponseEntity<TodoDTO> getTodo(@PathVariable("id") Long todoId) {
        TodoDTO todo = todoService.getTodo(todoId);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    //add get all todos
    @PreAuthorize("permitAll()")
    @GetMapping("getAllTodos")
    public ResponseEntity<List<TodoDTO>> getAllTodos() {
        List<TodoDTO> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    //add update todo
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("updateTodo/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@RequestBody TodoDTO todoDTO, @PathVariable("id") Long todoId) {
        TodoDTO updatedTodo = todoService.updateTodo(todoDTO, todoId);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    //add delete todo
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deleteTodo/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable("id") Long todoId) {
        todoService.deleteTodo(todoId);
        return new ResponseEntity<>("Todo deleted successfully", HttpStatus.OK);
    }

    //add complete todo
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("completeTodo/{id}")
    public ResponseEntity<TodoDTO> completeTodo(@PathVariable("id") Long todoId) {
        TodoDTO todo = todoService.completeTodo(todoId);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    //add incomplete todo
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("incompleteTodo/{id}")
    public ResponseEntity<TodoDTO> incompleteTodo(@PathVariable("id") Long todoId) {
        TodoDTO todo = todoService.incompleteTodo(todoId);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

}
