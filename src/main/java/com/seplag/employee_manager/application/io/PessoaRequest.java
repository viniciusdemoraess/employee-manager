package com.seplag.employee_manager.application.io;

import java.time.LocalDate;
import java.util.List;

public record PessoaRequest(
        String nome,
        LocalDate dataNascimento,
        String sexo,
        String mae,
        String pai,
        FotoPessoaRequest fotoPessoa,
        List<EnderecoRequest> enderecos
) {}
