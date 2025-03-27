package com.seplag.employee_manager.application.io;

import java.time.LocalDate;

public record FotoPessoaRequest(
        LocalDate data,
        String bucket,
        String hash
) {}