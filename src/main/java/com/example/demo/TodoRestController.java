package com.example.demo;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("todos")
public class TodoRestController {

    private final TodoRepository repository;

    public TodoRestController(final TodoRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public Flux<Todo> lerTodos(){
        return repository.findAll();
    }

    @GetMapping("{feito}")
    public Flux<Todo> lerByFeito(@PathVariable boolean feito){
        return repository.findByFeito(feito);
    }

    @PostMapping
    public Mono<Todo> criar(@RequestBody Todo todo){
        return repository.save(todo);
    }
    @DeleteMapping("{id}")
        public Mono<Void>deletar(@PathVariable String id){
            return repository.deleteById(id);
        }
    @PutMapping("{id}")
    public Mono<Todo> atualizar(@PathVariable String id){
        return repository
                .findById(id)
                .map(todoAtual-> new Todo(
                        id,
                        todoAtual.titulo(),
                        todoAtual.descricao(),
                        !todoAtual.feito()))
                .flatMap(repository::save)
                .onTerminateDetach();
    }
}
