package br.com.todolist.aws_lambda_todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todolist.aws_lambda_todo.dto.TodoRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.TodoResponseDTO;
import br.com.todolist.aws_lambda_todo.service.TodoService;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponseDTO> create(@RequestBody TodoRequestDTO requestDTO) {
        TodoResponseDTO createdTodo = todoService.create(requestDTO);
        return ResponseEntity.ok(createdTodo);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> listAll() {
        List<TodoResponseDTO> allDTOs = todoService.listAll();
        return ResponseEntity.ok(allDTOs);
    }






}
