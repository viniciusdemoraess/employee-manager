package com.seplag.employee_manager.application.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seplag.employee_manager.application.io.EnderecoRequest;
import com.seplag.employee_manager.application.io.EnderecoResponse;
import com.seplag.employee_manager.domain.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "Endpoints para controle de endereços.")
public class EnderecoRestAdapter {

    private final EnderecoService service;

    @Operation(summary = "Listar todos os endereços.", description = "Retorna uma lista paginada de todos os endereços.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<EnderecoResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
    }

    @Operation(summary = "Criar um novo endereço.", description = "Cria um novo endereço com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<EnderecoResponse> create(@RequestBody @Valid EnderecoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }


    @Operation(summary = "Atualizar um endereço", description = "Atualiza um endereço com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid EnderecoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deletar um endereço.", description = "Remove um endereço com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Endereço por ID", description = "Retorna os dados de um Endereço com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado!"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> getServidorEfetivo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getResponse(id));
    } 
   
}
