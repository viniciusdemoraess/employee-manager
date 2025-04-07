package com.seplag.employee_manager.application.io;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServidorEfetivoPerUnitResponse {
    
    String nome;

    int idade;

    String unidadeLotacao;

    String foto;

}
