package br.com.todolist.aws_lambda_todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.todolist.aws_lambda_todo.repository.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info(">>> [AuthorizationService] Buscando usuário no banco de dados: '{}'", username);

        UserDetails user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                logger.warn(">>> [AuthorizationService] Usuário '{}' NÃO ENCONTRADO no banco!", username);
                return new UsernameNotFoundException("Usuário não encontrado com o nome: " + username);
            });
        
        logger.info(">>> [AuthorizationService] Usuário '{}' encontrado com sucesso!", username);
        return user;
    }
}
