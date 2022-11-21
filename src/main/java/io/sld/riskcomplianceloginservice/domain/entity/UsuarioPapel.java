package io.sld.riskcomplianceloginservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UsuarioPapel.
 */
@Entity
@Table(name = "usuario_papel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioPapel implements Serializable {

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
    @Column(name = "idn_var_papel", nullable = false)
    private String idnVarPapel;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Papel papel;

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
    }

    public UsuarioPapel usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
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
