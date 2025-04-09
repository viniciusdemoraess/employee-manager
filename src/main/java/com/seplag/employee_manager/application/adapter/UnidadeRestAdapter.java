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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seplag.employee_manager.application.io.UnidadeRequest;
import com.seplag.employee_manager.application.io.UnidadeResponse;
import com.seplag.employee_manager.domain.entity.Unidade;
import com.seplag.employee_manager.domain.service.UnidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "Endpoints para gerenciamento de unidades")
public class UnidadeRestAdapter {

    private final UnidadeService unidadeService;

    /**
     * Retorna uma lista paginada de unidades.
     *
     * @param pageable Parâmetros de paginação.
     * @return Página contendo as unidades.
     */
    @Operation(summary = "Listar unidades", description = "Retorna uma lista paginada de unidades", tags = "Unidades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de unidades retornada com sucesso")
    })
    @GetMapping
    public Page<UnidadeResponse> getUnits(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return unidadeService.getUnits(PageRequest.of(page, size));
    }


    /**
     * Retorna uma unidade específica pelo ID.
     *
     * @param id ID da unidade.
     * @return Unidade encontrada.
     */
    @Operation(summary = "Buscar unidade por ID", description = "Retorna uma unidade específica pelo ID", tags = "Unidades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unidade encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> getUnitById(@PathVariable Long id) {;
        return ResponseEntity.ok(unidadeService.getUnitById(id));
    }

    /**
     * Cria uma nova unidade.
     *
     * @param unidade Dados da unidade a ser criada.
     * @return Unidade criada.
     */
    @Operation(summary = "Criar unidade", description = "Cria uma nova unidade", tags = "Unidades")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Unidade criada com sucesso",
            content = @Content(schema = @Schema(implementation = Unidade.class))
        )
    })
    @PostMapping
    public ResponseEntity<UnidadeResponse> createUnit(@RequestBody final UnidadeRequest unidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeService.createUnit(unidade));
    }

    
    /**
     * Atualiza uma unidade existente.
     *
     * @param id ID da unidade a ser atualizada.
     * @param unidade Dados atualizados da unidade.
     * @return Unidade atualizada.
     */
    @Operation(summary = "Atualizar unidade", description = "Atualiza uma unidade existente", tags = "Unidades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unidade atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> updateUnit(@PathVariable Long id, @RequestBody UnidadeRequest unidade) {
        return ResponseEntity.ok(unidadeService.updateUnit(id, unidade));
    }


    /**
     * Remove uma unidade.
     *
     * @param id ID da unidade a ser removida.
     */
    @Operation(summary = "Deletar unidade", description = "Remove uma unidade pelo ID", tags = "Unidades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Unidade removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        unidadeService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }

}
