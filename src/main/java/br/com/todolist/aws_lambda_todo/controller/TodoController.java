package br.com.todolist.aws_lambda_todo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.todolist.aws_lambda_todo.model.Todo;
import br.com.todolist.aws_lambda_todo.repository.TodoRepository;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository todoRepository;

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo newTodo) {
        logger.info(">>> Recebida requisição POST para criar item.");
        newTodo.setId(null);
        Todo savedTodo = todoRepository.save(newTodo);
        logger.info(">>> Item criado com sucesso com ID: {}", savedTodo.getId());
        return ResponseEntity.ok(savedTodo);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> listAll() {
        logger.info(">>> Recebida requisição GET para listar todos os itens.");
        return ResponseEntity.ok(todoRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        logger.info(">>> Recebida requisição PUT para o ID: {}", id);
        
        Optional<Todo> optionalTodo = todoRepository.findById(id);

        if (optionalTodo.isPresent()) {
            logger.info(">>> SUCESSO: Item com ID {} foi encontrado no banco!", id);
            Todo existingTodo = optionalTodo.get();
            existingTodo.setDescription(updatedTodo.getDescription());
            existingTodo.setDone(updatedTodo.isDone());
            Todo savedTodo = todoRepository.save(existingTodo);
            logger.info(">>> Item com ID {} foi atualizado e salvo.", id);
            return ResponseEntity.ok(savedTodo);
        } else {
            logger.warn(">>> FALHA: Item com ID {} NÃO foi encontrado no banco!", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        logger.info(">>> Recebida requisição DELETE para o ID: {}", id);
        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
