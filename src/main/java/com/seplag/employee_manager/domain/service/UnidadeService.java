package com.seplag.employee_manager.domain.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.CidadeRequest;
import com.seplag.employee_manager.application.io.LotacaoResponse;
import com.seplag.employee_manager.application.io.PessoaResponse;
import com.seplag.employee_manager.application.io.UnidadeRequest;
import com.seplag.employee_manager.application.io.UnidadeResponse;
import com.seplag.employee_manager.application.mappers.CidadeMapper;
import com.seplag.employee_manager.application.mappers.UnidadeMapper;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;
import com.seplag.employee_manager.domain.entity.Unidade;
import com.seplag.employee_manager.domain.entity.UnidadeEndereco;
import static com.seplag.employee_manager.util.BeansUtil.copyNonNullProperties;
import com.seplag.employee_manager.infrastructure.pesistence.CidadeRepository;
import com.seplag.employee_manager.infrastructure.pesistence.EnderecoRepository;
import com.seplag.employee_manager.infrastructure.pesistence.UnidadeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UnidadeService {
    
    private final UnidadeRepository unidadeRepository;

    private final UnidadeMapper mapper;

    private final CidadeMapper cidadeMapper;

    private final EnderecoRepository enderecoRepository;

    private final CidadeRepository cidadeRepository;

    /**
     * Retorna uma lista paginada de unidades.
     *
     * @param pageable Parâmetros de paginação.
     * @return Página contendo as unidades.
     */
    public Page<UnidadeResponse> getUnits(Pageable pageable) {
        return unidadeRepository.findAll(pageable)
            .map(unidade -> {
                Endereco endereco = unidade.getUnidadeEnderecos().isEmpty()
                    ? new Endereco()
                    : unidade.getUnidadeEnderecos().get(0).getEndereco();
    
                return mapToResponse(unidade, endereco);
            });
    }


    /**
     * Cria uma nova unidade.
     *
     * @param unidade Dados da unidade a ser criada.
     * @return Unidade criada.
     */
    @Transactional
    public UnidadeResponse createUnit(UnidadeRequest unidadeRequest) {

        Unidade unidade = mapper.toEntity(unidadeRequest);
        Cidade cidade = cidadeMapper.toEntity(unidadeRequest.cidade());
        cidade = cidadeRepository.save(cidade);

        Endereco endereco = new Endereco(null, unidadeRequest.tipoLogradouro(), unidadeRequest.logradouro(),
                        unidadeRequest.numero(), unidadeRequest.bairro(), cidade, null);

        endereco = enderecoRepository.save(endereco);
        unidade.adicionarEndereco(endereco);
        Unidade unidadeSalva = unidadeRepository.save(unidade);

        return mapToResponse(unidadeSalva, endereco);
    }

    /**
     * Atualiza uma unidade existente.
     *
     * @param id ID da unidade a ser atualizada.
     * @param unidade Dados atualizados da unidade.
     * @return Unidade atualizada.
     */
    public UnidadeResponse updateUnit(Long id, UnidadeRequest unidadeRequest) {
        Unidade unidadeExistente = unidadeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada"));
    
        copyNonNullProperties(unidadeRequest, unidadeExistente);
    
        Cidade cidade = salvarCidade(unidadeRequest.cidade());
        cidade = cidadeRepository.save(cidade);

        Endereco endereco = new Endereco();
        if (!unidadeExistente.getUnidadeEnderecos().isEmpty()) {
            UnidadeEndereco unidadeEndereco = unidadeExistente.getUnidadeEnderecos().get(0);
            endereco = unidadeEndereco.getEndereco();
    
            endereco.setTipoLogradouro(unidadeRequest.tipoLogradouro());
            endereco.setLogradouro(unidadeRequest.logradouro());
            endereco.setNumero(unidadeRequest.numero());
            endereco.setBairro(unidadeRequest.bairro());
            endereco.setCidade(cidade);
    
            enderecoRepository.save(endereco);
        } else {
            endereco = new Endereco(null,
                    unidadeRequest.tipoLogradouro(),
                    unidadeRequest.logradouro(),
                    unidadeRequest.numero(),
                    unidadeRequest.bairro(),
                    cidade,
                    null);
    
            endereco = enderecoRepository.save(endereco);
    
            UnidadeEndereco unidadeEndereco = new UnidadeEndereco(null, unidadeExistente, endereco);
            unidadeExistente.setUnidadeEnderecos(List.of(unidadeEndereco));
        }
    
        Unidade unidadeAtualizada = unidadeRepository.save(unidadeExistente);
    
        return mapToResponse(unidadeAtualizada, endereco);
    }
    


    /**
     * Remove uma unidade pelo ID.
     *
     * @param id ID da unidade a ser removida.
     */
    public void deleteUnit(Long id) {
        getUnitById(id);
        unidadeRepository.deleteById(id);
    }

    /**
     * Retorna uma unidade específica pelo ID.
     *
     * @param id ID da unidade.
     * @return Unidade encontrada.
     */
    public UnidadeResponse getUnitById(Long id) {
        Unidade unidade = unidadeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada"));
        UnidadeEndereco unidadeEndereco = unidade.getUnidadeEnderecos().get(0);
        

        return mapToResponse(unidade, unidadeEndereco.getEndereco());
    }



    private UnidadeResponse mapToResponse(Unidade unidade, Endereco endereco) {
        List<LotacaoResponse> lotacoes = unidade.getLotacoes().stream()
            .map(lotacao -> new LotacaoResponse(
                lotacao.getId(),
                new PessoaResponse(
                    lotacao.getPessoa().getId(),
                    lotacao.getPessoa().getNome()
                ),
                lotacao.getDataLotacao(),
                lotacao.getDataRemocao(),
                lotacao.getPortaria()
            ))
            .toList();

        UnidadeResponse response = new UnidadeResponse();
        response.setId(unidade.getId());
        response.setNome(unidade.getNome());
        response.setSigla(unidade.getSigla());
        response.setTipoLogradouro(endereco.getTipoLogradouro());
        response.setLogradouro(endereco.getLogradouro());
        response.setNumero(endereco.getNumero());
        response.setBairro(endereco.getBairro());

        if (!unidade.getUnidadeEnderecos().isEmpty()) {
            Cidade cidade = endereco.getCidade();
            CidadeRequest cidadeRequest = cidadeMapper.toResponse(cidade);
            response.setCidade(cidadeRequest);
        }

        response.setLotacoes(lotacoes);
        return response;
    }

    private Cidade salvarCidade(CidadeRequest request) {
        return cidadeRepository.findByNomeIgnoreCaseAndUfIgnoreCase(
                    request.nome(), request.uf()
                )
                .orElseGet(() -> {
                    Cidade nova = new Cidade();
                    nova.setNome(request.nome());
                    nova.setUf(request.uf());
                    return cidadeRepository.save(nova);
                });
    }

}
