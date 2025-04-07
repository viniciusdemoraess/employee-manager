package com.seplag.employee_manager.infrastructure.pesistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.FotoPessoa;
import com.seplag.employee_manager.domain.entity.Pessoa;

@Repository
public interface FotoPessoaRepository extends JpaRepository<FotoPessoa, Long> {

    List<FotoPessoa> findByPessoa(Pessoa pessoa);

}
