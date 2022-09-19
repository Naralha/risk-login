package io.sld.riskcomplianceloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UsuarioPapel.
 */
@Table("usuario_papel")
public class UsuarioPapel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("idn_var_usuario_cadastrado")
    private String idnVarUsuarioCadastrado;

    @NotNull(message = "must not be null")
    @Column("idn_var_papel")
    private String idnVarPapel;

    @NotNull(message = "must not be null")
    @Column("idn_var_usuario")
    private String idnVarUsuario;

    @Transient
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Papel papel;

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

    @Column("papel_id")
    private Long papelId;

    @Column("usuario_id")
    private Long usuarioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UsuarioPapel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarUsuarioCadastrado() {
        return this.idnVarUsuarioCadastrado;
    }

    public UsuarioPapel idnVarUsuarioCadastrado(String idnVarUsuarioCadastrado) {
        this.setIdnVarUsuarioCadastrado(idnVarUsuarioCadastrado);
        return this;
    }

    public void setIdnVarUsuarioCadastrado(String idnVarUsuarioCadastrado) {
        this.idnVarUsuarioCadastrado = idnVarUsuarioCadastrado;
    }

    public String getIdnVarPapel() {
        return this.idnVarPapel;
    }

    public UsuarioPapel idnVarPapel(String idnVarPapel) {
        this.setIdnVarPapel(idnVarPapel);
        return this;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public UsuarioPapel idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public Papel getPapel() {
        return this.papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
        this.papelId = papel != null ? papel.getId() : null;
    }

    public UsuarioPapel papel(Papel papel) {
        this.setPapel(papel);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario != null ? usuario.getId() : null;
    }

    public UsuarioPapel usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Long getPapelId() {
        return this.papelId;
    }

    public void setPapelId(Long papel) {
        this.papelId = papel;
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
        if (!(o instanceof UsuarioPapel)) {
            return false;
        }
        return id != null && id.equals(((UsuarioPapel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioPapel{" +
            "id=" + getId() +
            ", idnVarUsuarioCadastrado='" + getIdnVarUsuarioCadastrado() + "'" +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            "}";
    }
}
