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

import com.seplag.employee_manager.application.io.CidadeRequest;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.service.CidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/cidades")
@Tag(name = "Cidades", description = "Endpoints para controle de cidades.")
public class CidadeRestAdapter {

    private final CidadeService service;

    @Operation(summary = "Listar todas as Cidades.", description = "Retorna uma lista paginada de todas as Cidades.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<Cidade>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
    }

    @Operation(summary = "Criar uma nova cidade.", description = "Cria uma nova cidade com os dados fornecidos. Caso a mesma já exita, o registro existente é retornado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cidade criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Cidade> create(@RequestBody @Valid CidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar uma cidade", description = "Atualiza uma cidade com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cidade atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cidade> update(
            @PathVariable Long id,
            @RequestBody @Valid CidadeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deletar uma Cidade.", description = "Remove uma Cidade com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cidade deletada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Cidade por ID", description = "Retorna os dados de uma Cidade com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cidade encontrada!"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cidade> getServidorEfetivo(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    } 

}
