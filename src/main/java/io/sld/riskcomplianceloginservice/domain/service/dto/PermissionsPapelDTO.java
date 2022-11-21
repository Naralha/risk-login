package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link PermissionsPapel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionsPapelDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarPermissions;

    @NotNull
    private String idnVarPapel;

    @NotNull
    private String idnVarFeatures;

    @NotNull
    private String idnVarUsuario;

    private PapelDTO papel;

    private PermissionsDTO permissions;

    private FeaturesDTO features;

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

    public String getIdnVarPapel() {
        return idnVarPapel;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public String getIdnVarFeatures() {
        return idnVarFeatures;
    }

    public void setIdnVarFeatures(String idnVarFeatures) {
        this.idnVarFeatures = idnVarFeatures;
    }

    public String getIdnVarUsuario() {
        return idnVarUsuario;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public PapelDTO getPapel() {
        return papel;
    }

    public void setPapel(PapelDTO papel) {
        this.papel = papel;
    }

    public PermissionsDTO getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionsDTO permissions) {
        this.permissions = permissions;
    }

    public FeaturesDTO getFeatures() {
        return features;
    }

    public void setFeatures(FeaturesDTO features) {
        this.features = features;
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
        if (!(o instanceof PermissionsPapelDTO)) {
            return false;
        }

        PermissionsPapelDTO permissionsPapelDTO = (PermissionsPapelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissionsPapelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionsPapelDTO{" +
            "id=" + getId() +
            ", idnVarPermissions='" + getIdnVarPermissions() + "'" +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", idnVarFeatures='" + getIdnVarFeatures() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", papel=" + getPapel() +
            ", permissions=" + getPermissions() +
            ", features=" + getFeatures() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
