package com.example.todo_management.service.impl;

import com.example.todo_management.dto.TodoDTO;
import com.example.todo_management.entity.Todo;
import com.example.todo_management.exception.ResourceNotFoundException;
import com.example.todo_management.repository.TodoRepository;
import com.example.todo_management.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private ModelMapper modelMapper;

    @Override
    public TodoDTO addTodo(TodoDTO todoDTO) {

        //Convert TodoDTO into Todo Jpa entity
//        Todo todo = new Todo();
//        todo.setTitle(todo.getTitle());
//        todo.setDescription(todo.getDescription());
//        todo.setCompleted(todo.isCompleted());

        //instead of above code we can use ModelMapper

        Todo todo = modelMapper.map(todoDTO, Todo.class);

        //Todo Jpa entity
        Todo savedTodo = todoRepository.save(todo);

        //Convert saved Todo Jpa entity object into TodoDto object
//        TodoDTO savedTodoDTO = new TodoDTO();
//        savedTodoDTO.setId(savedTodo.getId());
//        savedTodoDTO.setTitle(savedTodo.getTitle());
//        savedTodoDTO.setDescription(savedTodo.getDescription());
//        savedTodoDTO.setCompleted(savedTodo.isCompleted());

        //instead of above code we can use ModelMapper

        TodoDTO savedTodoDTO = modelMapper.map(savedTodo, TodoDTO.class);

        return savedTodoDTO;
    }

    @Override
    public TodoDTO getTodo(Long id) {

        Todo todo = todoRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " + id));

        //Convert Todo Jpa entity into TodoDTO
        TodoDTO todoDTO = modelMapper.map(todo, TodoDTO.class);

        return todoDTO;

    }

    @Override
    public List<TodoDTO> getAllTodos() {

        List<Todo> todos = todoRepository.findAll();

        //Convert Todo Jpa entity into TodoDTO
        List<TodoDTO> todosDTO = todos.stream().map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());

        return todosDTO;
    }

    @Override
    public TodoDTO updateTodo(TodoDTO todoDTO, Long id) {

        Todo todo = todoRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " + id));

        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.isCompleted());

        Todo updatedTodo = todoRepository.save(todo);

        TodoDTO updatedTodoDTO = modelMapper.map(updatedTodo, TodoDTO.class);

        return updatedTodoDTO;
    }

    @Override
    public void deleteTodo(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " + id));

        todoRepository.deleteById(id);
    }

    @Override
    public TodoDTO completeTodo(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " + id));

        todo.setCompleted(Boolean.TRUE);

        Todo updatedTodo = todoRepository.save(todo);

        TodoDTO updatedTodoDTO = modelMapper.map(updatedTodo, TodoDTO.class);

        return updatedTodoDTO;
    }

    @Override
    public TodoDTO incompleteTodo(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " + id));

        todo.setCompleted(Boolean.FALSE);

        Todo updatedTodo = todoRepository.save(todo);

        TodoDTO updatedTodoDTO = modelMapper.map(updatedTodo, TodoDTO.class);

        return updatedTodoDTO;
    }
}
