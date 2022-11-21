package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.EmpresaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link App} and its DTO {@link AppDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppMapper extends EntityMapper<AppDTO, App> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    AppDTO toDto(App s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
