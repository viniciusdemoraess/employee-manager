package com.seplag.employee_manager.application.io;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServidorEfetivoRequest(

    @NotBlank
    @Size(max = 200)
    String nome,

    @NotNull
    LocalDate dataNascimento,

    @NotBlank
    @Size(max = 9)
    String sexo,

    @Size(max = 200)
    String mae,

    @Size(max = 200)
    String pai,

    @Size(max = 50)
    String tipoLogradouro,

    @Size(max = 200)
    String logradouro,

    Integer numero,

    @Size(max = 100)
    String bairro,

    @NotNull
    CidadeRequest cidade,

    @NotBlank
    String matricula,

    @NotNull
    LocalDate dataLotacao,

    @Size(max = 100)
    String portaria

) {}