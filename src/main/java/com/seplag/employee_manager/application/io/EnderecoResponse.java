package com.seplag.employee_manager.application.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnderecoResponse {

    private Long id;
    
    private String tipoLogradouro;
    
    private String logradouro;
    
    private Long numero;
    
    private String bairro;
    
    private String cidade;

}
