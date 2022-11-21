package io.sld.riskcomplianceloginservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Grupo.
 */
@Entity
@Table(name = "grupo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Grupo implements Serializable {

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
    @Column(name = "n_var_nome", nullable = false)
    private String nVarNome;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @NotNull
    @Column(name = "idn_var_empresa", nullable = false)
    private String idnVarEmpresa;

    @OneToMany(mappedBy = "grupo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupo", "papel", "empresa", "usuario" }, allowSetters = true)
    private Set<GrupoPapel> grupoPapels = new HashSet<>();

    @OneToMany(mappedBy = "grupo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupo", "usuario" }, allowSetters = true)
    private Set<UsuarioGrupo> usuarioGrupos = new HashSet<>();

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

    public Grupo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarGrupo() {
        return this.idnVarGrupo;
    }

    public Grupo idnVarGrupo(String idnVarGrupo) {
        this.setIdnVarGrupo(idnVarGrupo);
        return this;
    }

    public void setIdnVarGrupo(String idnVarGrupo) {
        this.idnVarGrupo = idnVarGrupo;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public Grupo nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public Grupo idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public String getIdnVarEmpresa() {
        return this.idnVarEmpresa;
    }

    public Grupo idnVarEmpresa(String idnVarEmpresa) {
        this.setIdnVarEmpresa(idnVarEmpresa);
        return this;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public Set<GrupoPapel> getGrupoPapels() {
        return this.grupoPapels;
    }

    public void setGrupoPapels(Set<GrupoPapel> grupoPapels) {
        if (this.grupoPapels != null) {
            this.grupoPapels.forEach(i -> i.setGrupo(null));
        }
        if (grupoPapels != null) {
            grupoPapels.forEach(i -> i.setGrupo(this));
        }
        this.grupoPapels = grupoPapels;
    }

    public Grupo grupoPapels(Set<GrupoPapel> grupoPapels) {
        this.setGrupoPapels(grupoPapels);
        return this;
    }

    public Grupo addGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.add(grupoPapel);
        grupoPapel.setGrupo(this);
        return this;
    }

    public Grupo removeGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.remove(grupoPapel);
        grupoPapel.setGrupo(null);
        return this;
    }

    public Set<UsuarioGrupo> getUsuarioGrupos() {
        return this.usuarioGrupos;
    }

    public void setUsuarioGrupos(Set<UsuarioGrupo> usuarioGrupos) {
        if (this.usuarioGrupos != null) {
            this.usuarioGrupos.forEach(i -> i.setGrupo(null));
        }
        if (usuarioGrupos != null) {
            usuarioGrupos.forEach(i -> i.setGrupo(this));
        }
        this.usuarioGrupos = usuarioGrupos;
    }

    public Grupo usuarioGrupos(Set<UsuarioGrupo> usuarioGrupos) {
        this.setUsuarioGrupos(usuarioGrupos);
        return this;
    }

    public Grupo addUsuarioGrupo(UsuarioGrupo usuarioGrupo) {
        this.usuarioGrupos.add(usuarioGrupo);
        usuarioGrupo.setGrupo(this);
        return this;
    }

    public Grupo removeUsuarioGrupo(UsuarioGrupo usuarioGrupo) {
        this.usuarioGrupos.remove(usuarioGrupo);
        usuarioGrupo.setGrupo(null);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Grupo empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Grupo usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Grupo)) {
            return false;
        }
        return id != null && id.equals(((Grupo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Grupo{" +
            "id=" + getId() +
            ", idnVarGrupo='" + getIdnVarGrupo() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            "}";
    }
}
