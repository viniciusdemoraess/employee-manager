package com.seplag.employee_manager.domain.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServidorTemporarioResponse {

    
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

    LocalDate dataAdmissao;

    LocalDate dataDemissao;

    LocalDate dataLotacao;

    String portaria;

    List<String> fotos;
    
}
