package com.seplag.employee_manager.application.io;

public record EnderecoRequest(
    String tipoLogradouro,
    String logradouro,
    Long numero,
    String bairro,
    CidadeRequest cidade
){}
