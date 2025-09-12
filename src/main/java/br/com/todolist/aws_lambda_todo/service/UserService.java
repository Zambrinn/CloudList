package br.com.todolist.aws_lambda_todo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.todolist.aws_lambda_todo.dto.UserRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.UserResponseDTO;
import br.com.todolist.aws_lambda_todo.exception.ResourceNotFoundException;
import br.com.todolist.aws_lambda_todo.model.User;
import br.com.todolist.aws_lambda_todo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO create(UserRequestDTO requestDTO) {
        User user = User.builder()
                .username(requestDTO.username())
                .password(requestDTO.password())
                .build();

        return convertoToDTO(user);
    }

    public List<UserResponseDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertoToDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO update(UUID id, UserRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com o ID: " + id + " não encontrado"));

        existingUser.setUsername(requestDTO.username());
        existingUser.setPassword(requestDTO.password());
        User updatedUser = userRepository.save(existingUser);
        return convertoToDTO(updatedUser);
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário com o ID: " + id + " não encontrado");
        }
    }

    private UserResponseDTO convertoToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername());
    }

}
