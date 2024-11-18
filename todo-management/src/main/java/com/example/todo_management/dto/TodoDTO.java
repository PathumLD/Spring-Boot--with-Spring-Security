package com.example.todo_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoDTO {

    private Long id;
    private String title;
    private String description;
    private boolean completed;

}
