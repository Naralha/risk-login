package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link GrupoPapel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GrupoPapelDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarGrupo;

    @NotNull
    private String idnVarPapel;

    @NotNull
    private String idnVarUsuario;

    @NotNull
    private String idnVarEmpresa;

    private GrupoDTO grupo;

    private PapelDTO papel;

    private EmpresaDTO empresa;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarGrupo() {
        return idnVarGrupo;
    }

    public void setIdnVarGrupo(String idnVarGrupo) {
        this.idnVarGrupo = idnVarGrupo;
    }

    public String getIdnVarPapel() {
        return idnVarPapel;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public String getIdnVarUsuario() {
        return idnVarUsuario;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public String getIdnVarEmpresa() {
        return idnVarEmpresa;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public GrupoDTO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoDTO grupo) {
        this.grupo = grupo;
    }

    public PapelDTO getPapel() {
        return papel;
    }

    public void setPapel(PapelDTO papel) {
        this.papel = papel;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GrupoPapelDTO)) {
            return false;
        }

        GrupoPapelDTO grupoPapelDTO = (GrupoPapelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, grupoPapelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GrupoPapelDTO{" +
            "id=" + getId() +
            ", idnVarGrupo='" + getIdnVarGrupo() + "'" +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            ", grupo=" + getGrupo() +
            ", papel=" + getPapel() +
            ", empresa=" + getEmpresa() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
