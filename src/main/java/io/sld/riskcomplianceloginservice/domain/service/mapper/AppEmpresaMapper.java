package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppEmpresaDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.EmpresaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppEmpresa} and its DTO {@link AppEmpresaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppEmpresaMapper extends EntityMapper<AppEmpresaDTO, AppEmpresa> {
    @Mapping(target = "app", source = "app", qualifiedByName = "appId")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    AppEmpresaDTO toDto(AppEmpresa s);

    @Named("appId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppDTO toDtoAppId(App app);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
