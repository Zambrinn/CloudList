package br.com.todolist.aws_lambda_todo.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record TodoRequestDTO(
        @NotBlank(message = "A descrição não pode estar em branco") String description,
        boolean done,
        UUID user_id) {
}
