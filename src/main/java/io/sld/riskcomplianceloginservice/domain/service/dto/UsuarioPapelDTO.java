package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link UsuarioPapel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioPapelDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarUsuarioCadastrado;

    @NotNull
    private String idnVarPapel;

    @NotNull
    private String idnVarUsuario;

    private PapelDTO papel;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarUsuarioCadastrado() {
        return idnVarUsuarioCadastrado;
    }

    public void setIdnVarUsuarioCadastrado(String idnVarUsuarioCadastrado) {
        this.idnVarUsuarioCadastrado = idnVarUsuarioCadastrado;
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

    public PapelDTO getPapel() {
        return papel;
    }

    public void setPapel(PapelDTO papel) {
        this.papel = papel;
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
        if (!(o instanceof UsuarioPapelDTO)) {
            return false;
        }

        UsuarioPapelDTO usuarioPapelDTO = (UsuarioPapelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioPapelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioPapelDTO{" +
            "id=" + getId() +
            ", idnVarUsuarioCadastrado='" + getIdnVarUsuarioCadastrado() + "'" +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", papel=" + getPapel() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
