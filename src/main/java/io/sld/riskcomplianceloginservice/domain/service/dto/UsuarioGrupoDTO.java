package io.sld.riskcomplianceloginservice.domain.service.dto;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link UsuarioGrupo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioGrupoDTO implements Serializable {

    private Long id;

    @NotNull
    private String idnVarUsuarioCadastrado;

    @NotNull
    private String idnVarGrupo;

    @NotNull
    private String idnVarUsuario;

    private GrupoDTO grupo;

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

    public String getIdnVarGrupo() {
        return idnVarGrupo;
    }

    public void setIdnVarGrupo(String idnVarGrupo) {
        this.idnVarGrupo = idnVarGrupo;
    }

    public String getIdnVarUsuario() {
        return idnVarUsuario;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public GrupoDTO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoDTO grupo) {
        this.grupo = grupo;
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
        if (!(o instanceof UsuarioGrupoDTO)) {
            return false;
        }

        UsuarioGrupoDTO usuarioGrupoDTO = (UsuarioGrupoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioGrupoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioGrupoDTO{" +
            "id=" + getId() +
            ", idnVarUsuarioCadastrado='" + getIdnVarUsuarioCadastrado() + "'" +
            ", idnVarGrupo='" + getIdnVarGrupo() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", grupo=" + getGrupo() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
