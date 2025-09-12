package br.com.todolist.aws_lambda_todo.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username) {
}
