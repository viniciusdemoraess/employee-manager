package com.seplag.employee_manager.application.adapter;



import java.io.InputStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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

import com.seplag.employee_manager.application.io.ServidorEfetivoPerUnitResponse;
import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.application.io.ServidorEfetivoResponse;
import com.seplag.employee_manager.domain.service.ServidorEfetivoService;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/servidores-efetivos")
@AllArgsConstructor
@Tag(name = "Servidores Efetivos", description = "Endpoints para gerenciamento de Servidores Efetivos")
public class ServidorEfetivoRestAdapter {

    private final ServidorEfetivoService service;

    @Operation(summary = "Listar todos os servidores efetivos", description = "Retorna uma lista paginada de todos os servidores efetivos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ServidorEfetivoResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
    }

    @Operation(summary = "Criar um novo servidor efetivo", description = "Cria um novo servidor efetivo com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servidor criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServidorEfetivoResponse> create(@ModelAttribute @Valid ServidorEfetivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar servidor efetivo", description = "Atualiza um servidor efetivo com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServidorEfetivoResponse> update(
            @PathVariable Long id,
            @ModelAttribute @Valid ServidorEfetivoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
    
    @Operation(summary = "Deletar servidor efetivo", description = "Remove um servidor efetivo com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Servidor deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Buscar servidor efetivo por ID", description = "Retorna os dados de um servidor efetivo com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor encontrado"),
        @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServidorEfetivoResponse> getServidorEfetivo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    @GetMapping("/file/{fileName}")
    @Hidden
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) {
        try {
            InputStream stream = service.getImage(fileName);

            byte[] content = stream.readAllBytes();
            stream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Listar servidores por unidade", description = "Retorna uma lista paginada de servidores efetivos filtrada por unidade.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @GetMapping("/unidade/{unidId}")
    public ResponseEntity<Page<ServidorEfetivoPerUnitResponse>> listarPorUnidade(@PathVariable Long unidId, 
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listarPorUnidade(unidId, PageRequest.of(page, size)));
    }


    
}
