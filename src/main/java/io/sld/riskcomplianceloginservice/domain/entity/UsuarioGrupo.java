package io.sld.riskcomplianceloginservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UsuarioGrupo.
 */
@Entity
@Table(name = "usuario_grupo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioGrupo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idn_var_usuario_cadastrado", nullable = false)
    private String idnVarUsuarioCadastrado;

    @NotNull
    @Column(name = "idn_var_grupo", nullable = false)
    private String idnVarGrupo;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarioGrupos", "empresa", "usuario" }, allowSetters = true)
    private Grupo grupo;

    @ManyToOne
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
    }

    public UsuarioGrupo usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
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
