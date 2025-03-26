package com.seplag.employee_manager.application.adapter;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seplag.employee_manager.domain.service.ServidorService;

@RestController
@RequestMapping("/api/servidores")
public class ServidorRestAdapter {

    private final ServidorService servidorService;

    public ServidorRestAdapter(ServidorService servidorService) {
        this.servidorService = servidorService;
    }

    @GetMapping
    public ResponseEntity getServidor() {
        return ResponseEntity.ok("Hello World!");
    }


    // @PostMapping
    // public ResponseEntity<Servidor> createServidor(@RequestBody Servidor servidor) {
    //     return ResponseEntity.status(HttpStatus.CREATED).body(servidorService.create(servidor));
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<Servidor> updateServidor(@PathVariable Long id, @RequestBody Servidor servidor) {
    //     return ResponseEntity.ok(servidorService.update(id, servidor));
    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<Servidor> getServidor(@PathVariable Long id) {
    //     return ResponseEntity.ok(servidorService.get(id));
    // }

    // @GetMapping("/unidade/{unidId}")
    // public ResponseEntity<List<Servidor>> getServidoresByUnidade(@PathVariable Long unidId) {
    //     return ResponseEntity.ok(servidorService.getByUnidade(unidId));
    // }
}
