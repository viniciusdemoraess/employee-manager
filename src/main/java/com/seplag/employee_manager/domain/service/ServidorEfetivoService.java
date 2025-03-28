package com.seplag.employee_manager.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;
import com.seplag.employee_manager.domain.entity.Lotacao;
import com.seplag.employee_manager.domain.entity.Pessoa;
import com.seplag.employee_manager.domain.entity.PessoaEndereco;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;
import com.seplag.employee_manager.infrastructure.pesistence.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final CidadeRepository cidadeRepository;
  
    @Transactional
    public ServidorEfetivo create(ServidorEfetivoRequest request) {
        
        Cidade cidade = new Cidade(null, request.cidade().nome(), request.cidade().uf());

        Cidade cidadeSalva = cidadeRepository.save(cidade);

        Endereco endereco = new Endereco(null, request.tipoLogradouro(), request.logradouro(), request.numero(),
            request.bairro(), cidadeSalva, null);

        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        // Lotacao lotacao = new Lotacao();

        // PessoaEndereco pessoaEndereco = new PessoaEndereco(null, , enderecoSalvo);
        

        ServidorEfetivo servidor = new ServidorEfetivo();

        servidor.setNome(request.nome());
        servidor.setDataNascimento(request.dataNascimento());
        servidor.setSexo(request.sexo());
        servidor.setMae(request.mae());
        servidor.setPai(request.pai());
        servidor.setPessoaEnderecos(List.of(pessoaEndereco));

        servidor.setMatricula(request.matricula());

        return repository.save(servidor);
    }

    public ServidorEfetivo getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Servidor Efetivo não encontrado"));
    }
    
}
