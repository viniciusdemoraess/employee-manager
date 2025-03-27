package com.seplag.employee_manager.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.seplag.employee_manager.application.io.LotacaoRequest;
import com.seplag.employee_manager.domain.entity.Lotacao;

@Mapper
public interface LotacaoMapper {
    LotacaoMapper INSTANCE = Mappers.getMapper(LotacaoMapper.class);

    @Mapping(target = "pessoa", source = "dto.pessoa")
    @Mapping(target = "unidade", source = "dto.unidade")
    Lotacao toEntity(LotacaoRequest dto);
}
