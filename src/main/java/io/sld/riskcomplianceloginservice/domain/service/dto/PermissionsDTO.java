package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.Permissions;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Permissions} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionsDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarPermissions;

    @NotNull
    private String nVarNome;

    @NotNull
    private String nVarTipoPermissao;

    @NotNull
    private String idnVarUsuario;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarPermissions() {
        return idnVarPermissions;
    }

    public void setIdnVarPermissions(String idnVarPermissions) {
        this.idnVarPermissions = idnVarPermissions;
    }

    public String getnVarNome() {
        return nVarNome;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getnVarTipoPermissao() {
        return nVarTipoPermissao;
    }

    public void setnVarTipoPermissao(String nVarTipoPermissao) {
        this.nVarTipoPermissao = nVarTipoPermissao;
    }

    public String getIdnVarUsuario() {
        return idnVarUsuario;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
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
        if (!(o instanceof PermissionsDTO)) {
            return false;
        }

        PermissionsDTO permissionsDTO = (PermissionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionsDTO{" +
            "id=" + getId() +
            ", idnVarPermissions='" + getIdnVarPermissions() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", nVarTipoPermissao='" + getnVarTipoPermissao() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", usuario=" + getUsuario() +
            "}";
    }
}
