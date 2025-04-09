package com.seplag.employee_manager.domain.service;

import static com.seplag.employee_manager.util.BeansUtil.copyNonNullProperties;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.EnderecoRequest;
import com.seplag.employee_manager.application.io.EnderecoResponse;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;
import com.seplag.employee_manager.infrastructure.pesistence.CidadeRepository;
import com.seplag.employee_manager.infrastructure.pesistence.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EnderecoService {

    private final EnderecoRepository repository;
    private final CidadeRepository cidadeRepository;


    @Transactional 
    public EnderecoResponse create(EnderecoRequest request) {

        Cidade cidade = salvarCidade(request);

        Endereco endereco = new Endereco(
            null,
            request.tipoLogradouro(),
            request.logradouro(),
            request.numero(),
            request.bairro(),
            cidade,
            null);
        
        return mapToResponse(repository.save(endereco));
    }

    @Transactional 
    public EnderecoResponse update(Long id, EnderecoRequest request) {
        Endereco endereco = get(id);
        Cidade cidade = salvarCidade(request);

        Endereco temp = new Endereco();
        temp.setTipoLogradouro(request.tipoLogradouro());
        temp.setLogradouro(request.logradouro());
        temp.setNumero(request.numero());
        temp.setBairro(request.bairro());
        temp.setCidade(cidade);
        temp.setPessoaEnderecos(
            endereco.getPessoaEnderecos() == null ? new ArrayList<>() : endereco.getPessoaEnderecos()
        );


        copyNonNullProperties(temp, endereco);
        
        return mapToResponse(repository.save(endereco));
    }

    public Endereco get(Long id){
        return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Endereço com ID: " + id + ", não encontrado."));
    }

    public EnderecoResponse getResponse(Long id) {
        return mapToResponse(get(id));
    }

    private EnderecoResponse mapToResponse(Endereco endereco) {
        return new EnderecoResponse(
            endereco.getId(),
            endereco.getTipoLogradouro(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getBairro(),
            endereco.getCidade().getNome()
        );
    }

    public Page<EnderecoResponse> getAll(Pageable page) {
        return repository.findAll(page)
            .map(this::mapToResponse);
    }

    private Cidade salvarCidade(EnderecoRequest request) {
        return cidadeRepository.findByNomeIgnoreCaseAndUfIgnoreCase(
                    request.cidade().nome(), request.cidade().uf()
                )
                .orElseGet(() -> {
                    Cidade nova = new Cidade();
                    nova.setNome(request.cidade().nome());
                    nova.setUf(request.cidade().uf());
                    return cidadeRepository.save(nova);
                });
    }

    public void delete(Long id) {
        repository.delete(get(id));
    }
    
}
