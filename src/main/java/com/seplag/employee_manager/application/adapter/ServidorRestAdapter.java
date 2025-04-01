package com.seplag.employee_manager.application.adapter;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;
import com.seplag.employee_manager.domain.service.ServidorEfetivoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/servidores")
@AllArgsConstructor
@Tag(name = "Servidores", description = "Endpoints para gerenciamento de Servidores")
public class ServidorRestAdapter {

    private final ServidorEfetivoService efetivoService;
    // private final ServidorTemporarioService temporarioService;


    @GetMapping
    public ResponseEntity getServidor() {
        return ResponseEntity.ok("Hello World!");
    }


    @PostMapping("/efetivo")
    public ResponseEntity<ServidorEfetivo> createServidorEfetivo(@RequestBody ServidorEfetivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(efetivoService.create(request));
    }

    // @PostMapping("/temporario")
    // public ResponseEntity<ServidorTemporario> createServidorTemporario(@RequestBody ServidorTemporarioRequest request) {
    //     return ResponseEntity.status(HttpStatus.CREATED).body(temporarioService.create(request));
    // }

    @GetMapping("/efetivo/{id}")
    public ResponseEntity<ServidorEfetivo> getServidorEfetivo(@PathVariable Long id) {
        return ResponseEntity.ok(efetivoService.getById(id));
    }

    // @GetMapping("/temporario/{id}")
    // public ResponseEntity<ServidorTemporario> getServidorTemporario(@PathVariable Long id) {
    //     return ResponseEntity.ok(temporarioService.getById(id));
    // }
}
