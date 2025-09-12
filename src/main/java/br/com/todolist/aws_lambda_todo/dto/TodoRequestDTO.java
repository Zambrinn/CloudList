package br.com.todolist.aws_lambda_todo.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoRequestDTO(
        @NotBlank(message = "A descrição não pode estar em branco") String description,
        boolean done) {
}
