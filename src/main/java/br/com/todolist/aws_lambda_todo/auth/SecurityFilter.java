package br.com.todolist.aws_lambda_todo.auth; 

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.todolist.aws_lambda_todo.repository.UserRepository;
import br.com.todolist.aws_lambda_todo.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(">>> [SecurityFilter] Filtro iniciado para a rota: {}", request.getRequestURI());
        var token = this.recoverToken(request);

        if (token != null) {
            logger.info(">>> [SecurityFilter] Token encontrado no cabeçalho.");
            try {
                var userIdString = tokenService.getSubjectFromToken(token);
                logger.info(">>> [SecurityFilter] Token válido. Subject (UserID): {}", userIdString);

                UUID userId = UUID.fromString(userIdString);
                var userOptional = userRepository.findById(userId);

                if (userOptional.isPresent()) {
                    var user = userOptional.get();
                    logger.info(">>> [SecurityFilter] Usuário '{}' encontrado no banco.", user.getUsername());
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info(">>> [SecurityFilter] Usuário autenticado e salvo no SecurityContextHolder.");
                } else {
                    logger.warn(">>> [SecurityFilter] Usuário com ID {} não encontrado no banco.", userId);
                }
            } catch (Exception e) {
                logger.error(">>> [SecurityFilter] Erro na validação do token: {}", e.getMessage());
            }
        } else {
            logger.info(">>> [SecurityFilter] Nenhum token encontrado no cabeçalho 'Authorization'.");
        }

        filterChain.doFilter(request, response);
        logger.info(">>> [SecurityFilter] Filtro finalizado para a rota: {}", request.getRequestURI());
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}