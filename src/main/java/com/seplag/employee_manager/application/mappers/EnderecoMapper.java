package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.seplag.employee_manager.application.io.EnderecoRequest;
import com.seplag.employee_manager.domain.entity.Endereco;

@Mapper
public interface EnderecoMapper {
    EnderecoMapper INSTANCE = Mappers.getMapper(EnderecoMapper.class);

    @Mapping(target = "tipoLogradouro", source = "dto.tipoLogradouro")
    @Mapping(target = "logradouro", source = "dto.logradouro")
    @Mapping(target = "numero", source = "dto.numero")
    @Mapping(target = "bairro", source = "dto.bairro")
    @Mapping(target = "cidade", source = "dto.cidade")
    Endereco toEntity(EnderecoRequest dto);
}
