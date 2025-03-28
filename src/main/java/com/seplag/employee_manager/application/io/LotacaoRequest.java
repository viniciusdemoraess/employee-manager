package com.seplag.employee_manager.application.io;

public record LotacaoRequest(
    PessoaRequest pessoa,
    UnidadeRequest unidade
) {}
