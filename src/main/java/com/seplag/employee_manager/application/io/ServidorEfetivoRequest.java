package com.seplag.employee_manager.application.io;

public record ServidorEfetivoRequest(
    PessoaRequest pessoa,
    EnderecoRequest endereco,
    LotacaoRequest lotacao,
    String matricula
) {}