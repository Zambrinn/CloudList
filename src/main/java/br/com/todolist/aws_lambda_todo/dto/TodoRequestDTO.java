package br.com.todolist.aws_lambda_todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoRequestDTO {
    @NotBlank(message = "A descrição não pode estar em branco")
    private String description;
    private boolean done;
}
