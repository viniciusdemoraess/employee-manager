package com.seplag.employee_manager.application.io;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeResponse {

    private Long id;
    
    private String nome;

    private String sigla;

    private String tipoLogradouro;

    private String logradouro;

    private Long numero;

    private String bairro;

    private List<LotacaoResponse> lotacoes;

    private CidadeRequest cidade;

}
