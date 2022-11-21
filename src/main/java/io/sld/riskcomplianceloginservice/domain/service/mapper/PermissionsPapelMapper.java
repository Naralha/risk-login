package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissionsPapel} and its DTO {@link PermissionsPapelDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionsPapelMapper extends EntityMapper<PermissionsPapelDTO, PermissionsPapel> {
    @Mapping(target = "papel", source = "papel", qualifiedByName = "papelId")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "permissionsId")
    @Mapping(target = "features", source = "features", qualifiedByName = "featuresId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    PermissionsPapelDTO toDto(PermissionsPapel s);

    @Named("papelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PapelDTO toDtoPapelId(Papel papel);

    @Named("permissionsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionsDTO toDtoPermissionsId(Permissions permissions);

    @Named("featuresId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FeaturesDTO toDtoFeaturesId(Features features);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
