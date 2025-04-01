package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.seplag.employee_manager.application.io.UnidadeRequest;
import com.seplag.employee_manager.domain.entity.Unidade;

@Mapper(componentModel = "spring")
public interface UnidadeMapper {

    
    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "nome", source = "request.nome") 
    @Mapping(target = "sigla", source = "request.sigla") 
    @Mapping(target = "lotacoes", ignore = true) 
    @Mapping(target = "unidadeEnderecos", ignore = true) 
    Unidade toEntity(UnidadeRequest request);

    
}
