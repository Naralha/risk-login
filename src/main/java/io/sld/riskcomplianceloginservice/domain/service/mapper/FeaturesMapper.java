package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Features} and its DTO {@link FeaturesDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeaturesMapper extends EntityMapper<FeaturesDTO, Features> {
    @Mapping(target = "app", source = "app", qualifiedByName = "appId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    FeaturesDTO toDto(Features s);

    @Named("appId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppDTO toDtoAppId(App app);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
