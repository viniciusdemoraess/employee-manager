package com.seplag.employee_manager.application.io;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LotacaoResponse {
    
    private Long id;

    private PessoaResponse pessoa;

    private LocalDate dataLotacao;

    private LocalDate dataRemocao;

    private String portaria;

   }
