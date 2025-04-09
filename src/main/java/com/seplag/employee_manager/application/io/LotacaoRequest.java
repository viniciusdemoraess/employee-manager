package com.seplag.employee_manager.application.io;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LotacaoRequest(
    @NotNull
    Long pessoaId,
    
    @NotNull
    Long unidadeId,
    
    @NotNull
    LocalDate dataLotacao,
    
    LocalDate dataRemocao,
    
    @Size(max = 100)
    String portaria

) {}
