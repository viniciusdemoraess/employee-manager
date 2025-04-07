package com.seplag.employee_manager.infrastructure.pesistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidade(
            String logradouro, Long numero, String bairro, Cidade cidade);


 }
