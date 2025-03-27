package com.seplag.employee_manager.application.adapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seplag.employee_manager.application.io.LoginRequest;
import com.seplag.employee_manager.application.io.LoginResponse;
import com.seplag.employee_manager.infrastructure.auth.AuthenticationService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationRestAdapter {

    private final AuthenticationService authenticationService;

    /**
     * Autentica um usuário com base nas credenciais fornecidas.
     *
     * @param loginRequest Dados de autenticação (email e senha).
     *
     * @return Token JWT gerado para o usuário autenticado.
     */
    @Operation(summary = "Autenticação de usuário", description = "Autentica um usuário e retorna um token JWT", tags = "Autenticação")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário autenticado com sucesso",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
        @Parameter(description = "Dados para autenticação (email e senha)", required = true) @RequestBody final LoginRequest loginRequest
    ) {
        final var response = this.authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

}




