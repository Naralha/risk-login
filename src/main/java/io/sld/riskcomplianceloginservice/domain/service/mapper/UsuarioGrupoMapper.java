package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioGrupoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UsuarioGrupo} and its DTO {@link UsuarioGrupoDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioGrupoMapper extends EntityMapper<UsuarioGrupoDTO, UsuarioGrupo> {
    @Mapping(target = "grupo", source = "grupo", qualifiedByName = "grupoId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    UsuarioGrupoDTO toDto(UsuarioGrupo s);

    @Named("grupoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GrupoDTO toDtoGrupoId(Grupo grupo);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
