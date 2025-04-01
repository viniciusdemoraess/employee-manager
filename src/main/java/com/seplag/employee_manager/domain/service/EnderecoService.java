package com.seplag.employee_manager.domain.service;

import org.springframework.stereotype.Service;

import com.seplag.employee_manager.infrastructure.pesistence.EnderecoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    
}
