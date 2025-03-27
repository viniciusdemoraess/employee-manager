package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;

@Mapper(uses = {PessoaMapper.class, EnderecoMapper.class, LotacaoMapper.class})
public interface ServidorEfetivoMapper {
    ServidorEfetivoMapper INSTANCE = Mappers.getMapper(ServidorEfetivoMapper.class);

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "matricula", source = "request.matricula")
    // @Mapping(target = "pessoa", source = "request.pessoa") // Mapeia a Pessoa
    // @Mapping(target = "endereco", source = "request.endereco") // Mapeia o Endereco
    // @Mapping(target = "lotacao", source = "request.lotacao") // Mapeia a Lotacao
    ServidorEfetivo toEntity(ServidorEfetivoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matricula", source = "newData.matricula")
    // @Mapping(target = "pessoa", source = "newData.pessoa")
    // @Mapping(target = "endereco", source = "newData.endereco")
    // @Mapping(target = "lotacao", source = "newData.lotacao")
    void updateEntity(ServidorEfetivo newData, @MappingTarget ServidorEfetivo oldData);
}
