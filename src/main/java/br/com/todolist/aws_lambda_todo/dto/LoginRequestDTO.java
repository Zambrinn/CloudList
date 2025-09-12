package br.com.todolist.aws_lambda_todo.dto;

public record LoginRequestDTO(
    String username,
    String password
) {}