package com.seplag.employee_manager.application.io;

public record EnderecoFuncionalResponse(
    String servidor,
    String unidade,
    String logradouro,
    Long numero,
    String bairro,
    String cidade,
    String uf
) {}