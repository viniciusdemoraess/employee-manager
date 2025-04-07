package com.seplag.employee_manager.infrastructure.pesistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.Pessoa;
import com.seplag.employee_manager.domain.entity.PessoaEndereco;

@Repository
public interface PessoaEnderecoRepository extends JpaRepository<PessoaEndereco, Long> { 

    List<PessoaEndereco> findByPessoa(Pessoa pessoa);


}