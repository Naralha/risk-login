package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioPapelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UsuarioPapel} and its DTO {@link UsuarioPapelDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioPapelMapper extends EntityMapper<UsuarioPapelDTO, UsuarioPapel> {
    @Mapping(target = "papel", source = "papel", qualifiedByName = "papelId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    UsuarioPapelDTO toDto(UsuarioPapel s);

    @Named("papelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PapelDTO toDtoPapelId(Papel papel);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
