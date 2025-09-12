package br.com.todolist.aws_lambda_todo.dto;

public record UserRequestDTO(
        String username,
        String password) {
}
