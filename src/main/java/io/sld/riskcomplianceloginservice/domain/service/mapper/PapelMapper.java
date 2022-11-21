package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Papel} and its DTO {@link PapelDTO}.
 */
@Mapper(componentModel = "spring")
public interface PapelMapper extends EntityMapper<PapelDTO, Papel> {
    @Mapping(target = "app", source = "app", qualifiedByName = "appId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    PapelDTO toDto(Papel s);

    @Named("appId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppDTO toDtoAppId(App app);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
