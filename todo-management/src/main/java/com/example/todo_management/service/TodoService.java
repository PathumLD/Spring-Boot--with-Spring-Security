package com.example.todo_management.service;

import com.example.todo_management.dto.TodoDTO;

import java.util.List;

public interface TodoService {

    TodoDTO addTodo(TodoDTO todoDTO);

    TodoDTO getTodo(Long id);

    List<TodoDTO> getAllTodos(); //List<Todo>

    TodoDTO updateTodo(TodoDTO todoDTO, Long id);

    void deleteTodo(Long id);

    TodoDTO completeTodo(Long id);

    TodoDTO incompleteTodo(Long id);
}
