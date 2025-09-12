package br.com.todolist.aws_lambda_todo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.todolist.aws_lambda_todo.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {
    
}
