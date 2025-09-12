package br.com.todolist.aws_lambda_todo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.todolist.aws_lambda_todo.dto.TodoRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.TodoResponseDTO;
import br.com.todolist.aws_lambda_todo.exception.ResourceNotFoundException;
import br.com.todolist.aws_lambda_todo.model.Todo;
import br.com.todolist.aws_lambda_todo.repository.TodoRepository;
import br.com.todolist.aws_lambda_todo.repository.UserRepository;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public TodoResponseDTO create(TodoRequestDTO requestDTO) {
        var user = userRepository.findById(requestDTO.user_id());
        Todo todo = Todo.builder()
        .description(requestDTO.description())
        .done(requestDTO.done())
        .user(user.get())
        .build();
    
        Todo savedTodo = todoRepository.save(todo);

        return convertToResponseDTO(savedTodo);
    }

    public List<TodoResponseDTO> listAll() {
        return todoRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TodoResponseDTO update(UUID id, TodoRequestDTO todoRequestDTO) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o ID: " + id));

        existingTodo.setDescription(todoRequestDTO.description());
        existingTodo.setDone(todoRequestDTO.done());
        Todo updatedTodo = todoRepository.save(existingTodo);
        return convertToResponseDTO(updatedTodo);
    }

    public void delete(UUID id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item não encontrado com o id: " + id);
        }
    }

    private TodoResponseDTO convertToResponseDTO(Todo todo) {
        return new TodoResponseDTO(
                todo.getId(),
                todo.getDescription(),
                todo.isDone());
    }
}
