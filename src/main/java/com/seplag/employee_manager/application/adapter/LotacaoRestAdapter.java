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

import com.seplag.employee_manager.application.io.LotacaoRequest;
import com.seplag.employee_manager.application.io.LotacaoResponse;
import com.seplag.employee_manager.domain.service.LotacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/lotacoes")
@Tag(name = "Lotações", description = "Endpoints para controle de lotações.")
public class LotacaoRestAdapter {

    private final LotacaoService service;

    @Operation(summary = "Listar todas as Lotações de servidores.", description = "Retorna uma lista paginada de todas as Lotações de servidores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<LotacaoResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
    }


    @Operation(summary = "Criar uma nova Lotação.", description = "Cria uma nova Lotação com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lotação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<LotacaoResponse> create(@RequestBody @Valid LotacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar uma nova Lotação", description = "Atualiza uma nova Lotação com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lotação atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Lotação não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LotacaoResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid LotacaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deletar uma lotação.", description = "Remove uma lotação com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Lotação deletada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Lotação não encontrada!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Lotação por ID", description = "Retorna os dados de uma Lotação com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lotação encontrada!"),
        @ApiResponse(responseCode = "404", description = "Lotação não encontrada!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LotacaoResponse> getServidorEfetivo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getResponse(id));
    }   
    
}
