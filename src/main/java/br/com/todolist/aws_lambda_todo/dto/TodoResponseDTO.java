package br.com.todolist.aws_lambda_todo.dto;

import lombok.Data;

@Data
public class TodoResponseDTO {
    private Long id;
    private String description;
    private boolean done;
}
