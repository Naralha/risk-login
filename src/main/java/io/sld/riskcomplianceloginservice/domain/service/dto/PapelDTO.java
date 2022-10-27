package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.Papel;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Papel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PapelDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarPapel;

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

    public String getIdnVarPapel() {
        return idnVarPapel;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
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
        if (!(o instanceof PapelDTO)) {
            return false;
        }

        PapelDTO papelDTO = (PapelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, papelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PapelDTO{" +
            "id=" + getId() +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarApp='" + getIdnVarApp() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", app=" + getApp() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
