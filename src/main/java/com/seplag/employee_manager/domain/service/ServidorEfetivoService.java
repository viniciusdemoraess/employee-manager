package com.seplag.employee_manager.domain.service;

import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.domain.entity.Pessoa;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;
import com.seplag.employee_manager.infrastructure.pesistence.EnderecoRepository;
import com.seplag.employee_manager.infrastructure.pesistence.LotacaoRepository;
import com.seplag.employee_manager.infrastructure.pesistence.PessoaRepository;
import com.seplag.employee_manager.infrastructure.pesistence.ServidorEfetivoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final LotacaoRepository lotacaoRepository;
    
    public ServidorEfetivo create(ServidorEfetivoRequest request) {
        // Pessoa pessoa = pessoaRepository.save(request.pessoa());
        // enderecoRepository.save(request.endereco());
        // lotacaoRepository.save(request.lotacao());

        ServidorEfetivo servidor = new ServidorEfetivo();
        servidor.setMatricula(request.matricula());
        // servidor.setId(pessoa.getId());

        return repository.save(servidor);
    }

    public ServidorEfetivo getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Servidor Efetivo não encontrado"));
    }
    
}
