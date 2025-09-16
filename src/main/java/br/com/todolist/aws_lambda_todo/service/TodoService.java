package br.com.todolist.aws_lambda_todo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.todolist.aws_lambda_todo.dto.TodoRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.TodoResponseDTO;
import br.com.todolist.aws_lambda_todo.exception.ResourceNotFoundException;
import br.com.todolist.aws_lambda_todo.model.Todo;
import br.com.todolist.aws_lambda_todo.model.User;
import br.com.todolist.aws_lambda_todo.repository.TodoRepository;
import br.com.todolist.aws_lambda_todo.repository.UserRepository;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public TodoResponseDTO create(TodoRequestDTO requestDTO) {
        User currentUser = getAuthenticatedUser();

        Todo todo = Todo.builder()
        .description(requestDTO.description())
        .done(requestDTO.done())
        .user(currentUser)
        .build();
    
        Todo savedTodo = todoRepository.save(todo);

        return convertToResponseDTO(savedTodo);
    }
    
    public List<TodoResponseDTO> findAllByUser() {
        User currentUser = getAuthenticatedUser();
   
        return todoRepository.findByUser_Id(currentUser.getId())
        .stream()
        .map(this::convertToResponseDTO)
        .collect(Collectors.toList());
    }

    public List<TodoResponseDTO> listAll() {
        return todoRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TodoResponseDTO update(UUID id, TodoRequestDTO todoRequestDTO) {
        User currentUser = getAuthenticatedUser();
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o ID: " + id));

        if (!existingTodo.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Acesso negado: você não é o dono deste item.");
        }               

        existingTodo.setDescription(todoRequestDTO.description());
        existingTodo.setDone(todoRequestDTO.done());
        Todo updatedTodo = todoRepository.save(existingTodo);
        return convertToResponseDTO(updatedTodo);
    }

    public void delete(UUID id) {
        User currentUser = getAuthenticatedUser();
        Todo todoToDelete = todoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o id: " + id));

        if (!todoToDelete.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Você não pode remover este item pois não é o dono dele.");
        }

        todoRepository.delete(todoToDelete);
    }

    private TodoResponseDTO convertToResponseDTO(Todo todo) {
        return new TodoResponseDTO(
                todo.getId(),
                todo.getDescription(),
                todo.isDone());
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}