package br.com.todolist.aws_lambda_todo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.todolist.aws_lambda_todo.dto.TodoRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.TodoResponseDTO;
import br.com.todolist.aws_lambda_todo.model.Todo;
import br.com.todolist.aws_lambda_todo.repository.TodoRepository;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public TodoResponseDTO create(TodoRequestDTO requestDTO) {
        Todo todo = new Todo();
        todo.setDescription(requestDTO.getDescription());
        todo.setDone(requestDTO.isDone());
        Todo savedTodo = todoRepository.save(todo);

        return convertToResponseDTO(savedTodo);
        
    }

    public List<TodoResponseDTO> listAll() {
        return todoRepository.findAll()
            .stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    public Optional<TodoResponseDTO> update(Long id, TodoRequestDTO todoRequestDTO) {
        return todoRepository.findById(id)
        .map(existingTodo -> {
            existingTodo.setDescription(todoRequestDTO.getDescription());
            existingTodo.setDone(todoRequestDTO.isDone());
            Todo updatedTodo = todoRepository.save(existingTodo);
            return convertToResponseDTO(updatedTodo);
        });
    }

    public boolean delete(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TodoResponseDTO convertToResponseDTO(Todo todo) {
        TodoResponseDTO responseDTO = new TodoResponseDTO();
        responseDTO.setId(todo.getId());
        responseDTO.setDescription(todo.getDescription());
        responseDTO.setId(todo.getId());

        return responseDTO;
    }
}
