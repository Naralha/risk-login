package io.sld.riskcomplianceloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UsuarioGrupo.
 */
@Table("usuario_grupo")
public class UsuarioGrupo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("idn_var_usuario_cadastrado")
    private String idnVarUsuarioCadastrado;

    @NotNull(message = "must not be null")
    @Column("idn_var_grupo")
    private String idnVarGrupo;

    @NotNull(message = "must not be null")
    @Column("idn_var_usuario")
    private String idnVarUsuario;

    @Transient
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarioGrupos", "empresa", "usuario" }, allowSetters = true)
    private Grupo grupo;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "apps",
            "appEmpresas",
            "features",
            "grupos",
            "grupoPapels",
            "permissions",
            "permissionsPapels",
            "papels",
            "usuarioGrupos",
            "usuarioPapels",
            "empresa",
        },
        allowSetters = true
    )
    private Usuario usuario;

    @Column("grupo_id")
    private Long grupoId;

    @Column("usuario_id")
    private Long usuarioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UsuarioGrupo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarUsuarioCadastrado() {
        return this.idnVarUsuarioCadastrado;
    }

    public UsuarioGrupo idnVarUsuarioCadastrado(String idnVarUsuarioCadastrado) {
        this.setIdnVarUsuarioCadastrado(idnVarUsuarioCadastrado);
        return this;
    }

    public void setIdnVarUsuarioCadastrado(String idnVarUsuarioCadastrado) {
        this.idnVarUsuarioCadastrado = idnVarUsuarioCadastrado;
    }

    public String getIdnVarGrupo() {
        return this.idnVarGrupo;
    }

    public UsuarioGrupo idnVarGrupo(String idnVarGrupo) {
        this.setIdnVarGrupo(idnVarGrupo);
        return this;
    }

    public void setIdnVarGrupo(String idnVarGrupo) {
        this.idnVarGrupo = idnVarGrupo;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public UsuarioGrupo idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public Grupo getGrupo() {
        return this.grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
        this.grupoId = grupo != null ? grupo.getId() : null;
    }

    public UsuarioGrupo grupo(Grupo grupo) {
        this.setGrupo(grupo);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario != null ? usuario.getId() : null;
    }

    public UsuarioGrupo usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Long getGrupoId() {
        return this.grupoId;
    }

    public void setGrupoId(Long grupo) {
        this.grupoId = grupo;
    }

    public Long getUsuarioId() {
        return this.usuarioId;
    }

    public void setUsuarioId(Long usuario) {
        this.usuarioId = usuario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioGrupo)) {
            return false;
        }
        return id != null && id.equals(((UsuarioGrupo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioGrupo{" +
            "id=" + getId() +
            ", idnVarUsuarioCadastrado='" + getIdnVarUsuarioCadastrado() + "'" +
            ", idnVarGrupo='" + getIdnVarGrupo() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            "}";
    }
}
