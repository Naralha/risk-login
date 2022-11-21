package io.sld.riskcomplianceloginservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GrupoPapel.
 */
@Entity
@Table(name = "grupo_papel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GrupoPapel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idn_var_grupo", nullable = false)
    private String idnVarGrupo;

    @NotNull
    @Column(name = "idn_var_papel", nullable = false)
    private String idnVarPapel;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @NotNull
    @Column(name = "idn_var_empresa", nullable = false)
    private String idnVarEmpresa;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarioGrupos", "empresa", "usuario" }, allowSetters = true)
    private Grupo grupo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Papel papel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarios", "grupos", "apps", "appEmpresas" }, allowSetters = true)
    private Empresa empresa;

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

    public GrupoPapel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarGrupo() {
        return this.idnVarGrupo;
    }

    public GrupoPapel idnVarGrupo(String idnVarGrupo) {
        this.setIdnVarGrupo(idnVarGrupo);
        return this;
    }

    public void setIdnVarGrupo(String idnVarGrupo) {
        this.idnVarGrupo = idnVarGrupo;
    }

    public String getIdnVarPapel() {
        return this.idnVarPapel;
    }

    public GrupoPapel idnVarPapel(String idnVarPapel) {
        this.setIdnVarPapel(idnVarPapel);
        return this;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public GrupoPapel idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public String getIdnVarEmpresa() {
        return this.idnVarEmpresa;
    }

    public GrupoPapel idnVarEmpresa(String idnVarEmpresa) {
        this.setIdnVarEmpresa(idnVarEmpresa);
        return this;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public Grupo getGrupo() {
        return this.grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public GrupoPapel grupo(Grupo grupo) {
        this.setGrupo(grupo);
        return this;
    }

    public Papel getPapel() {
        return this.papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public GrupoPapel papel(Papel papel) {
        this.setPapel(papel);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public GrupoPapel empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public GrupoPapel usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GrupoPapel)) {
            return false;
        }
        return id != null && id.equals(((GrupoPapel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GrupoPapel{" +
            "id=" + getId() +
            ", idnVarGrupo='" + getIdnVarGrupo() + "'" +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            "}";
    }
}
