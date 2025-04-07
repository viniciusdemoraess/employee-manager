package com.seplag.employee_manager.application.io;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServidorEfetivoResponse {

    Long id;
    
    String nome;

    LocalDate dataNascimento;

    String sexo;

    String mae;

    String pai;
    
    String tipoLogradouro;
    
    String logradouro;

    Long numero;

    String bairro;

    String cidade;

    String uf;

    String unidade;

    String matricula;

    LocalDate dataLotacao;

    String portaria;

    List<String> fotos;

}
