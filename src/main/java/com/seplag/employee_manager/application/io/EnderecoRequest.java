package com.seplag.employee_manager.application.io;

public record EnderecoRequest(
    String tipoLogradouro,
    String logradouro,
    Integer numero,
    String bairro,
    CidadeRequest cidade
){}
