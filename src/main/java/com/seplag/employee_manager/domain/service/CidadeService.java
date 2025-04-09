package com.seplag.employee_manager.domain.service;

import static com.seplag.employee_manager.util.BeansUtil.copyNonNullProperties;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.CidadeRequest;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.infrastructure.pesistence.CidadeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CidadeService {
    
    private final CidadeRepository repository;

    @Transactional 
    public Cidade create(CidadeRequest request) {

        return repository.findByNomeIgnoreCaseAndUfIgnoreCase(
                request.nome(), request.uf()
            )
            .orElseGet(() -> {
                Cidade nova = new Cidade();
                nova.setNome(request.nome());
                nova.setUf(request.uf());
                return repository.save(nova);
            });    
    }

    @Transactional
    public Cidade update (Long id, CidadeRequest request) {
        Cidade cidade = get(id);

        Cidade temp = new Cidade();
        temp.setNome(request.nome());
        temp.setUf(request.uf());

        copyNonNullProperties(temp, cidade);
        
        return repository.save(cidade);
    }

    public Page<Cidade> getAll(Pageable page) {
        return repository.findAll(page);
    }


    public Cidade get(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cidade com ID: " + id + ", não encontrada."));
    }

    public void delete(Long id) {
        repository.delete(get(id));
    }




}
