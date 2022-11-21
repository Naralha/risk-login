package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.EmpresaDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Grupo} and its DTO {@link GrupoDTO}.
 */
@Mapper(componentModel = "spring")
public interface GrupoMapper extends EntityMapper<GrupoDTO, Grupo> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    GrupoDTO toDto(Grupo s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
