package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.Grupo;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Grupo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GrupoDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarGrupo;

    @NotNull
    private String nVarNome;

    @NotNull
    private String idnVarUsuario;

    @NotNull
    private String idnVarEmpresa;

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

    public String getnVarNome() {
        return nVarNome;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
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
        if (!(o instanceof GrupoDTO)) {
            return false;
        }

        GrupoDTO grupoDTO = (GrupoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, grupoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GrupoDTO{" +
            "id=" + getId() +
            ", idnVarGrupo='" + getIdnVarGrupo() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            ", empresa=" + getEmpresa() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
