package com.seplag.employee_manager.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;
import com.seplag.employee_manager.domain.entity.Lotacao;
import com.seplag.employee_manager.domain.entity.PessoaEndereco;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;
import com.seplag.employee_manager.domain.entity.Unidade;
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
    private final PessoaEnderecoRepository pessoaEnderecoRepository;
    private final UnidadeRepository unidadeRepository;
  
    @Transactional
    public ServidorEfetivo create(ServidorEfetivoRequest request) {
        
        Cidade cidade = new Cidade(null, request.cidade().nome(), request.cidade().uf());

        Cidade cidadeSalva = cidadeRepository.save(cidade);

        Endereco endereco = new Endereco(null, request.tipoLogradouro(), request.logradouro(), request.numero(),
            request.bairro(), cidadeSalva, null);

        Endereco enderecoSalvo = enderecoRepository.save(endereco);       

        ServidorEfetivo servidor = new ServidorEfetivo();

        servidor.setNome(request.nome());
        servidor.setDataNascimento(request.dataNascimento());
        servidor.setSexo(request.sexo());
        servidor.setMae(request.mae());
        servidor.setPai(request.pai());
        servidor.setMatricula(request.matricula());
        ServidorEfetivo servidorSalvo = repository.save(servidor);

        PessoaEndereco pessoaEndereco = new PessoaEndereco(null, servidorSalvo, enderecoSalvo);
        pessoaEnderecoRepository.save(pessoaEndereco);
        
        servidorSalvo.setPessoaEnderecos(List.of(pessoaEndereco));

        Unidade unidade = new Unidade(null, request.unidade().nome(), request.unidade().sigla(), null, null);
        Unidade unidadeSalva = unidadeRepository.save(unidade);

        Lotacao lotacao = new Lotacao(null, servidorSalvo, unidadeSalva, request.dataLotacao(), null, request.portaria());

        Lotacao lotacaoSalva = lotacaoRepository.save(lotacao);

        unidadeSalva.getLotacoes().add(lotacaoSalva);
        unidadeRepository.save(unidadeSalva);

        return repository.save(servidor);
    }

    public ServidorEfetivo getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Servidor Efetivo não encontrado"));
    }
    
}
