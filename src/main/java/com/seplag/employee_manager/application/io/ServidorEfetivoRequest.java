package com.seplag.employee_manager.application.io;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.seplag.employee_manager.util.FotoValida;

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
    @NotBlank
    @Size(max = 200)
    String cidade,
    
    @NotNull
    @NotBlank
    @Size(max = 2)
    String uf,


    @NotNull
    Long unidadeId,

    @NotBlank
    String matricula,

    @NotNull
    LocalDate dataLotacao,

    @Size(max = 100)
    String portaria,

    @FotoValida
    List<MultipartFile> fotos

) {}