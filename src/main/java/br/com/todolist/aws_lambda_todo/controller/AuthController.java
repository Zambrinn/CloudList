package br.com.todolist.aws_lambda_todo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todolist.aws_lambda_todo.dto.LoginRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.LoginResponseDTO;
import br.com.todolist.aws_lambda_todo.dto.UserRequestDTO;
import br.com.todolist.aws_lambda_todo.dto.UserResponseDTO;
import br.com.todolist.aws_lambda_todo.model.User;
import br.com.todolist.aws_lambda_todo.service.TokenService;
import br.com.todolist.aws_lambda_todo.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth") 
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginData) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginData.username(), loginData.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO registerData) {
        UserResponseDTO createdUser = userService.create(registerData);
        return ResponseEntity.ok(createdUser);
    }
}