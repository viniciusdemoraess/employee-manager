package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.seplag.employee_manager.application.io.PessoaRequest;
import com.seplag.employee_manager.domain.entity.Pessoa;

@Mapper
public interface PessoaMapper {
    PessoaMapper INSTANCE = Mappers.getMapper(PessoaMapper.class);

    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "dataNascimento", source = "dto.dataNascimento")
    @Mapping(target = "sexo", source = "dto.sexo")
    @Mapping(target = "mae", source = "dto.mae")
    @Mapping(target = "pai", source = "dto.pai")
    // Se tiver outros campos, adicione aqui
    Pessoa toEntity(PessoaRequest dto);
}
