package com.seplag.employee_manager.application.io;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.seplag.employee_manager.util.FotoValida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServidorTemporarioRequest (

    
    @NotBlank(message = "O campo nome não pode estar em branco")
    @Size(max = 200)
    String nome,

    @NotNull
    LocalDate dataNascimento,

    @NotBlank(message = "O campo sexo não pode estar em branco")
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

    Long numero,

    @Size(max = 100)
    String bairro,

    @NotNull
    @NotBlank(message = "O campo cidade não pode estar em branco")
    @Size(max = 200)
    String cidade,
    
    @NotNull
    @NotBlank(message = "O campo UF não pode estar em branco")
    @Size(max = 2)
    String uf,


    @NotNull
    Long unidadeId,

    @NotNull
    LocalDate dataAdmissao,

    LocalDate dataDemissao,

    @NotNull
    LocalDate dataLotacao,

    @Size(max = 100)
    String portaria,

    @FotoValida
    List<MultipartFile> fotos
) {}
