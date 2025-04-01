package com.seplag.employee_manager.application.io;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UnidadeRequest(
    
    @NotNull
    @NotBlank
    @Size(max = 200)
    String nome,
    
    @NotNull
    @NotBlank
    @Size(max = 20)
    String sigla,

    @NotNull
    @NotBlank
    @Size(max = 50)
    String tipoLogradouro,

    @NotNull
    @NotBlank
    @Size(max = 200)
    String logradouro,

    Integer numero,

    @NotBlank
    @Size(max = 100)
    String bairro,

    @NotNull
    CidadeRequest cidade

) {}

