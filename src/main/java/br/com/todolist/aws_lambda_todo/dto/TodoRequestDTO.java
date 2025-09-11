package br.com.todolist.aws_lambda_todo.dto;

import lombok.Data;

@Data
public class TodoRequestDTO {
    private String description;
    private boolean done;
}
