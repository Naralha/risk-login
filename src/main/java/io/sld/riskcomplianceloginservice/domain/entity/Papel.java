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
 * A Papel.
 */
@Entity
@Table(name = "papel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Papel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idn_var_papel", nullable = false)
    private String idnVarPapel;

    @NotNull
    @Column(name = "n_var_nome", nullable = false)
    private String nVarNome;

    @NotNull
    @Column(name = "idn_var_app", nullable = false)
    private String idnVarApp;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @OneToMany(mappedBy = "papel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupo", "papel", "empresa", "usuario" }, allowSetters = true)
    private Set<GrupoPapel> grupoPapels = new HashSet<>();

    @OneToMany(mappedBy = "papel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "papel", "permissions", "features", "usuario" }, allowSetters = true)
    private Set<PermissionsPapel> permissionsPapels = new HashSet<>();

    @OneToMany(mappedBy = "papel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "papel", "usuario" }, allowSetters = true)
    private Set<UsuarioPapel> usuarioPapels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "appEmpresas", "features", "papels", "empresa", "usuario" }, allowSetters = true)
    private App app;

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

    public Papel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarPapel() {
        return this.idnVarPapel;
    }

    public Papel idnVarPapel(String idnVarPapel) {
        this.setIdnVarPapel(idnVarPapel);
        return this;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public Papel nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getIdnVarApp() {
        return this.idnVarApp;
    }

    public Papel idnVarApp(String idnVarApp) {
        this.setIdnVarApp(idnVarApp);
        return this;
    }

    public void setIdnVarApp(String idnVarApp) {
        this.idnVarApp = idnVarApp;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public Papel idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public Set<GrupoPapel> getGrupoPapels() {
        return this.grupoPapels;
    }

    public void setGrupoPapels(Set<GrupoPapel> grupoPapels) {
        if (this.grupoPapels != null) {
            this.grupoPapels.forEach(i -> i.setPapel(null));
        }
        if (grupoPapels != null) {
            grupoPapels.forEach(i -> i.setPapel(this));
        }
        this.grupoPapels = grupoPapels;
    }

    public Papel grupoPapels(Set<GrupoPapel> grupoPapels) {
        this.setGrupoPapels(grupoPapels);
        return this;
    }

    public Papel addGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.add(grupoPapel);
        grupoPapel.setPapel(this);
        return this;
    }

    public Papel removeGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.remove(grupoPapel);
        grupoPapel.setPapel(null);
        return this;
    }

    public Set<PermissionsPapel> getPermissionsPapels() {
        return this.permissionsPapels;
    }

    public void setPermissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        if (this.permissionsPapels != null) {
            this.permissionsPapels.forEach(i -> i.setPapel(null));
        }
        if (permissionsPapels != null) {
            permissionsPapels.forEach(i -> i.setPapel(this));
        }
        this.permissionsPapels = permissionsPapels;
    }

    public Papel permissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        this.setPermissionsPapels(permissionsPapels);
        return this;
    }

    public Papel addPermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.add(permissionsPapel);
        permissionsPapel.setPapel(this);
        return this;
    }

    public Papel removePermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.remove(permissionsPapel);
        permissionsPapel.setPapel(null);
        return this;
    }

    public Set<UsuarioPapel> getUsuarioPapels() {
        return this.usuarioPapels;
    }

    public void setUsuarioPapels(Set<UsuarioPapel> usuarioPapels) {
        if (this.usuarioPapels != null) {
            this.usuarioPapels.forEach(i -> i.setPapel(null));
        }
        if (usuarioPapels != null) {
            usuarioPapels.forEach(i -> i.setPapel(this));
        }
        this.usuarioPapels = usuarioPapels;
    }

    public Papel usuarioPapels(Set<UsuarioPapel> usuarioPapels) {
        this.setUsuarioPapels(usuarioPapels);
        return this;
    }

    public Papel addUsuarioPapel(UsuarioPapel usuarioPapel) {
        this.usuarioPapels.add(usuarioPapel);
        usuarioPapel.setPapel(this);
        return this;
    }

    public Papel removeUsuarioPapel(UsuarioPapel usuarioPapel) {
        this.usuarioPapels.remove(usuarioPapel);
        usuarioPapel.setPapel(null);
        return this;
    }

    public App getApp() {
        return this.app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Papel app(App app) {
        this.setApp(app);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Papel usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Papel)) {
            return false;
        }
        return id != null && id.equals(((Papel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Papel{" +
            "id=" + getId() +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarApp='" + getIdnVarApp() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            "}";
    }
}
