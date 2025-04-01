package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.seplag.employee_manager.application.io.EnderecoRequest;
import com.seplag.employee_manager.domain.entity.Endereco;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "cidade", ignore = true)
    @Mapping(target = "pessoaEnderecos", ignore = true) 
    @Mapping(target = "tipoLogradouro", source = "dto.tipoLogradouro")
    @Mapping(target = "logradouro", source = "dto.logradouro")
    @Mapping(target = "numero", source = "dto.numero")
    @Mapping(target = "bairro", source = "dto.bairro")
    Endereco toEntity(EnderecoRequest dto);
}
