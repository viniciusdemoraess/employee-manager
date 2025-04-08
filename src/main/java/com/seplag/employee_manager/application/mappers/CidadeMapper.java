package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.seplag.employee_manager.application.io.CidadeRequest;
import com.seplag.employee_manager.domain.entity.Cidade;

@Mapper(componentModel = "spring")
public interface CidadeMapper {
    

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "nome", source = "request.nome") 
    @Mapping(target = "uf", source = "request.uf") 
    Cidade toEntity(CidadeRequest request);


    
    @Mapping(target = "nome", source = "nome") 
    @Mapping(target = "uf", source = "uf") 
    CidadeRequest toResponse(Cidade cidade);
}
