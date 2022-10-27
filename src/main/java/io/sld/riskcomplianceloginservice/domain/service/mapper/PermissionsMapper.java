package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permissions} and its DTO {@link PermissionsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionsMapper extends EntityMapper<PermissionsDTO, Permissions> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    PermissionsDTO toDto(Permissions s);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
