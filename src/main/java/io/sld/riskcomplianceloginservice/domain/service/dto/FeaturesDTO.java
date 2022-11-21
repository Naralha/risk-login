package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.Features;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Features} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeaturesDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarFeatures;

    @NotNull
    private String nVarNome;

    @NotNull
    private String idnVarApp;

    @NotNull
    private String idnVarUsuario;

    private AppDTO app;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarFeatures() {
        return idnVarFeatures;
    }

    public void setIdnVarFeatures(String idnVarFeatures) {
        this.idnVarFeatures = idnVarFeatures;
    }

    public String getnVarNome() {
        return nVarNome;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getIdnVarApp() {
        return idnVarApp;
    }

    public void setIdnVarApp(String idnVarApp) {
        this.idnVarApp = idnVarApp;
    }

    public String getIdnVarUsuario() {
        return idnVarUsuario;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public AppDTO getApp() {
        return app;
    }

    public void setApp(AppDTO app) {
        this.app = app;
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
        if (!(o instanceof FeaturesDTO)) {
            return false;
        }

        FeaturesDTO featuresDTO = (FeaturesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, featuresDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeaturesDTO{" +
            "id=" + getId() +
            ", idnVarFeatures='" + getIdnVarFeatures() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarApp='" + getIdnVarApp() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", app=" + getApp() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
