package com.seplag.employee_manager.domain.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.io.UnidadeRequest;
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
    public Page<Unidade> getUnits(Pageable pageable) {
        return unidadeRepository.findAll(pageable);
    }


    /**
     * Cria uma nova unidade.
     *
     * @param unidade Dados da unidade a ser criada.
     * @return Unidade criada.
     */
    @Transactional
    public Unidade createUnit(UnidadeRequest unidadeRequest) {

        Unidade unidade = mapper.toEntity(unidadeRequest);
        Cidade cidade = cidadeMapper.toEntity(unidadeRequest.cidade());
        cidade = cidadeRepository.save(cidade);

        Endereco endereco = new Endereco(null, unidadeRequest.tipoLogradouro(), unidadeRequest.logradouro(),
                        unidadeRequest.numero(), unidadeRequest.bairro(), cidade, null);

        endereco = enderecoRepository.save(endereco);
        unidade.adicionarEndereco(endereco);

        return unidadeRepository.save(unidade);
    }

    /**
     * Atualiza uma unidade existente.
     *
     * @param id ID da unidade a ser atualizada.
     * @param unidade Dados atualizados da unidade.
     * @return Unidade atualizada.
     */
    public Unidade updateUnit(Long id, UnidadeRequest unidadeRequest) {
        Unidade unidadeExistente = getUnitById(id);

        copyNonNullProperties(unidadeRequest, unidadeExistente);

        Cidade cidade = cidadeMapper.toEntity(unidadeRequest.cidade());
        cidade = cidadeRepository.save(cidade);

        if (!unidadeExistente.getUnidadeEnderecos().isEmpty()) {
            UnidadeEndereco unidadeEndereco = unidadeExistente.getUnidadeEnderecos().get(0);
            Endereco endereco = unidadeEndereco.getEndereco();

            endereco.setTipoLogradouro(unidadeRequest.tipoLogradouro());
            endereco.setLogradouro(unidadeRequest.logradouro());
            endereco.setNumero(unidadeRequest.numero());
            endereco.setBairro(unidadeRequest.bairro());
            endereco.setCidade(cidade);

            enderecoRepository.save(endereco);
        } else {
            Endereco novoEndereco = new Endereco(null, unidadeRequest.tipoLogradouro(), unidadeRequest.logradouro(),
                    unidadeRequest.numero(), unidadeRequest.bairro(), cidade, null);
            novoEndereco = enderecoRepository.save(novoEndereco);

            UnidadeEndereco unidadeEndereco = new UnidadeEndereco(null, unidadeExistente, novoEndereco);
            unidadeExistente.setUnidadeEnderecos(List.of(unidadeEndereco));
        }

        return unidadeRepository.save(unidadeExistente);
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
    public Unidade getUnitById(Long id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada"));
    }

}
