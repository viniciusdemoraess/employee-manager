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
    @Mapping(target = "nome", source = "request.nome")
    @Mapping(target = "dataNascimento", source = "request.dataNascimento")
    @Mapping(target = "sexo", source = "request.sexo")
    @Mapping(target = "mae", source = "request.mae")
    @Mapping(target = "pai", source = "request.pai")
    ServidorEfetivo toEntity(ServidorEfetivoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matricula", source = "newData.matricula")
    // @Mapping(target = "pessoa", source = "newData.pessoa")
    // @Mapping(target = "endereco", source = "newData.endereco")
    // @Mapping(target = "lotacao", source = "newData.lotacao")
    void updateEntity(ServidorEfetivo newData, @MappingTarget ServidorEfetivo oldData);
}
