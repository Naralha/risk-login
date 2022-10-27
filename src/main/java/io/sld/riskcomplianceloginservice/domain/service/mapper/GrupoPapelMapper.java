package io.sld.riskcomplianceloginservice.domain.service.mapper;

import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.service.dto.EmpresaDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GrupoPapel} and its DTO {@link GrupoPapelDTO}.
 */
@Mapper(componentModel = "spring")
public interface GrupoPapelMapper extends EntityMapper<GrupoPapelDTO, GrupoPapel> {
    @Mapping(target = "grupo", source = "grupo", qualifiedByName = "grupoId")
    @Mapping(target = "papel", source = "papel", qualifiedByName = "papelId")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    GrupoPapelDTO toDto(GrupoPapel s);

    @Named("grupoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GrupoDTO toDtoGrupoId(Grupo grupo);

    @Named("papelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PapelDTO toDtoPapelId(Papel papel);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
