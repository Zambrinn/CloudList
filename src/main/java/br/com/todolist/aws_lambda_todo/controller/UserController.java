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

import br.com.todolist.aws_lambda_todo.dto.UserRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.UserResponseDTO;
import br.com.todolist.aws_lambda_todo.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.create(requestDTO));
    } 

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
       return ResponseEntity.ok(userService.listAll());
    }

    @PutMapping("/api/v1/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.update(id, requestDTO));
    }

    @DeleteMapping("/api/v1/{id}")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
