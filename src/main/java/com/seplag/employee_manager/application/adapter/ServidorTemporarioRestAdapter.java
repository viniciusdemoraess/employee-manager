package com.seplag.employee_manager.application.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seplag.employee_manager.application.io.ServidorTemporarioRequest;
import com.seplag.employee_manager.domain.entity.ServidorTemporarioResponse;
import com.seplag.employee_manager.domain.service.ServidorTemporarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/servidores-temporarios")
@AllArgsConstructor
@Tag(name = "Servidores Temporarios", description = "Endpoints para gerenciamento de Servidores Temporarios")
public class ServidorTemporarioRestAdapter {

    private final ServidorTemporarioService service;

    @Operation(summary = "Listar servidores temporários", description = "Retorna uma lista paginada de servidores temporários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ServidorTemporarioResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
    }


    @Operation(summary = "Criar servidor temporário", description = "Cria um novo servidor temporário com dados e arquivo(s) vinculados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servidor temporário criado com sucesso",
            content = @Content(schema = @Schema(implementation = ServidorTemporarioResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServidorTemporarioResponse> create(@ModelAttribute @Valid ServidorTemporarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }


    @Operation(summary = "Atualizar servidor temporário", description = "Atualiza um servidor temporário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor temporário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ServidorTemporarioResponse.class))),
        @ApiResponse(responseCode = "404", description = "Servidor temporário não encontrado")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServidorTemporarioResponse> update(
            @PathVariable Long id,
            @ModelAttribute @Valid ServidorTemporarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deletar servidor temporário", description = "Remove um servidor temporário pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Servidor temporário removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Servidor temporário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar servidor temporário por ID", description = "Retorna um servidor temporário específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor temporário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Servidor temporário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServidorTemporarioResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
}
