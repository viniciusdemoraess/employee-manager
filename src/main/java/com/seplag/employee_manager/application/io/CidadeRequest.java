package com.seplag.employee_manager.application.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CidadeRequest(

    @NotNull
    @NotBlank
    @Size(max = 200)
    String nome,
    
    @NotNull
    @NotBlank
    @Size(max = 2)
    String uf
) {}
