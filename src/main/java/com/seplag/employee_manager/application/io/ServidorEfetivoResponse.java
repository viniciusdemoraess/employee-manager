package com.seplag.employee_manager.application.io;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.seplag.employee_manager.util.FotoValida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServidorEfetivoResponse {
    
    String nome;

    // LocalDate dataNascimento;

    // String sexo;

    // String mae;

    // String pai;
    
    // String tipoLogradouro;

    
    // String logradouro;

    // Integer numero;


    // String bairro;


    // String cidade;
    

    // String uf;

    // String unidade;

    // String matricula;


    // LocalDate dataLotacao;

    // String portaria;


    List<String> fotos;


}
