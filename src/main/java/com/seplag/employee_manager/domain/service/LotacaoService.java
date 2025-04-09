package com.seplag.employee_manager.domain.service;

import static com.seplag.employee_manager.util.BeansUtil.copyNonNullProperties;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.LotacaoRequest;
import com.seplag.employee_manager.application.io.LotacaoResponse;
import com.seplag.employee_manager.application.io.PessoaResponse;
import com.seplag.employee_manager.domain.entity.Lotacao;
import com.seplag.employee_manager.domain.entity.Pessoa;
import com.seplag.employee_manager.domain.entity.Unidade;
import com.seplag.employee_manager.infrastructure.pesistence.LotacaoRepository;
import com.seplag.employee_manager.infrastructure.pesistence.PessoaRepository;
import com.seplag.employee_manager.infrastructure.pesistence.UnidadeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class LotacaoService {
    
    private final LotacaoRepository repository;
    private final UnidadeRepository unidadeRepository;
    private final PessoaRepository pessoaRepository;
    
    @Transactional
    public LotacaoResponse create(LotacaoRequest request) {
        Lotacao lotacao = new Lotacao();
        
        Unidade unidade = unidadeRepository.findById(request.unidadeId())
        .orElseThrow(() -> new EntityNotFoundException("Unidade com ID: " + request.unidadeId() + ", não encontrada."));
        
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
        .orElseThrow(() -> new EntityNotFoundException("Pessoa com ID: " + request.pessoaId() + ", não encontrada."));

        lotacao.setPessoa(pessoa);
        lotacao.setUnidade(unidade);
        lotacao.setDataLotacao(request.dataLotacao());
        lotacao.setDataRemocao(request.dataRemocao());
        lotacao.setPortaria(request.portaria());

        return mapToResponse(repository.save(lotacao));
    }

    @Transactional
    public LotacaoResponse update(Long id, LotacaoRequest request) {
        Lotacao lotacao = get(id);

        Unidade unidade = unidadeRepository.findById(request.unidadeId())
            .orElseThrow(() -> new EntityNotFoundException("Unidade com ID: " + request.unidadeId() + ", não encontrada."));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
            .orElseThrow(() -> new EntityNotFoundException("Pessoa com ID: " + request.pessoaId() + ", não encontrada."));

        Lotacao temp = new Lotacao();
        temp.setPessoa(pessoa);
        temp.setUnidade(unidade);
        temp.setDataLotacao(request.dataLotacao());
        temp.setDataRemocao(request.dataRemocao());
        temp.setPortaria(request.portaria());

        copyNonNullProperties(temp, lotacao);

        return mapToResponse(repository.save(lotacao));
    }



    /*@Transactional
    public LotacaoResponse update(Long id , LotacaoRequest request) {
        Lotacao lotacao = get(id);

        Unidade unidade = unidadeRepository.findById(request.unidadeId())
        .orElseThrow(() -> new EntityNotFoundException("Unidade com ID: " + request.unidadeId() + ", não encontrada."));
        
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
        .orElseThrow(() -> new EntityNotFoundException("Pessoa com ID: " + request.pessoaId() + ", não encontrada."));        

        lotacao.setPessoa(pessoa);
        lotacao.setUnidade(unidade);
        lotacao.setDataLotacao(request.dataLotacao());
        lotacao.setDataRemocao(request.dataRemocao());
        lotacao.setPortaria(request.portaria());

        return mapToResponse(repository.save(lotacao));

    }*/


    public Lotacao get(Long id){
        return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Lotação com ID: " + id + ", não encontrada."));
    }


    public LotacaoResponse getResponse(Long id) {
        return mapToResponse(get(id));
    }


    public Page<LotacaoResponse> getAll(Pageable page) {
        return repository.findAll(page)
            .map(this::mapToResponse);
    }

    private LotacaoResponse mapToResponse(Lotacao lotacao) {
        return new LotacaoResponse(
            lotacao.getId(),
            new PessoaResponse(lotacao.getPessoa().getId(),lotacao.getPessoa().getNome()),
            lotacao.getDataLotacao(),
            lotacao.getDataRemocao(),
            lotacao.getPortaria(), 
            lotacao.getUnidade().getNome());
    }

    public void delete(Long id) {
        repository.delete(get(id));
    }

}
