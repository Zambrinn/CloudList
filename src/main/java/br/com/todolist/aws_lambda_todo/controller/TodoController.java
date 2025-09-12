package br.com.todolist.aws_lambda_todo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todolist.aws_lambda_todo.dto.TodoRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.TodoResponseDTO;
import br.com.todolist.aws_lambda_todo.service.TodoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponseDTO> create(@Valid @RequestBody TodoRequestDTO requestDTO) {
        return ResponseEntity.ok(todoService.create(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> listAll() {
        return ResponseEntity.ok(todoService.listAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> update(@PathVariable UUID id,
            @Valid @RequestBody TodoRequestDTO requestDTO) {
        return ResponseEntity.ok(todoService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestBody TodoRequestDTO requestDTO) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}