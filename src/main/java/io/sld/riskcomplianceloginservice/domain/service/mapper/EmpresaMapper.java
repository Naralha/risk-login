package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.service.dto.EmpresaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empresa} and its DTO {@link EmpresaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpresaMapper extends EntityMapper<EmpresaDTO, Empresa> {}
