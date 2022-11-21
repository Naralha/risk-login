package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link AppEmpresa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppEmpresaDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarApp;

    @NotNull
    private String idnVarEmpresa;

    @NotNull
    private String idnVarUsuario;

    private AppDTO app;

    private EmpresaDTO empresa;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarApp() {
        return idnVarApp;
    }

    public void setIdnVarApp(String idnVarApp) {
        this.idnVarApp = idnVarApp;
    }

    public String getIdnVarEmpresa() {
        return idnVarEmpresa;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
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
        if (!(o instanceof AppEmpresaDTO)) {
            return false;
        }

        AppEmpresaDTO appEmpresaDTO = (AppEmpresaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appEmpresaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppEmpresaDTO{" +
            "id=" + getId() +
            ", idnVarApp='" + getIdnVarApp() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", app=" + getApp() +
            ", empresa=" + getEmpresa() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
