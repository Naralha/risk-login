package io.sld.riskcomplianceloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Empresa.
 */
@Table("empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("idn_var_empresa")
    private String idnVarEmpresa;

    @NotNull(message = "must not be null")
    @Column("n_var_nome")
    private String nVarNome;

    @Column("n_var_descricao")
    private String nVarDescricao;

    @Transient
    @JsonIgnoreProperties(value = { "grupo", "papel", "empresa", "usuario" }, allowSetters = true)
    private Set<GrupoPapel> grupoPapels = new HashSet<>();

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
    private Set<Usuario> usuarios = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarioGrupos", "empresa", "usuario" }, allowSetters = true)
    private Set<Grupo> grupos = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "appEmpresas", "features", "papels", "empresa", "usuario" }, allowSetters = true)
    private Set<App> apps = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "app", "empresa", "usuario" }, allowSetters = true)
    private Set<AppEmpresa> appEmpresas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empresa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarEmpresa() {
        return this.idnVarEmpresa;
    }

    public Empresa idnVarEmpresa(String idnVarEmpresa) {
        this.setIdnVarEmpresa(idnVarEmpresa);
        return this;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public Empresa nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getnVarDescricao() {
        return this.nVarDescricao;
    }

    public Empresa nVarDescricao(String nVarDescricao) {
        this.setnVarDescricao(nVarDescricao);
        return this;
    }

    public void setnVarDescricao(String nVarDescricao) {
        this.nVarDescricao = nVarDescricao;
    }

    public Set<GrupoPapel> getGrupoPapels() {
        return this.grupoPapels;
    }

    public void setGrupoPapels(Set<GrupoPapel> grupoPapels) {
        if (this.grupoPapels != null) {
            this.grupoPapels.forEach(i -> i.setEmpresa(null));
        }
        if (grupoPapels != null) {
            grupoPapels.forEach(i -> i.setEmpresa(this));
        }
        this.grupoPapels = grupoPapels;
    }

    public Empresa grupoPapels(Set<GrupoPapel> grupoPapels) {
        this.setGrupoPapels(grupoPapels);
        return this;
    }

    public Empresa addGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.add(grupoPapel);
        grupoPapel.setEmpresa(this);
        return this;
    }

    public Empresa removeGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.remove(grupoPapel);
        grupoPapel.setEmpresa(null);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.setEmpresa(null));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.setEmpresa(this));
        }
        this.usuarios = usuarios;
    }

    public Empresa usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Empresa addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.setEmpresa(this);
        return this;
    }

    public Empresa removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.setEmpresa(null);
        return this;
    }

    public Set<Grupo> getGrupos() {
        return this.grupos;
    }

    public void setGrupos(Set<Grupo> grupos) {
        if (this.grupos != null) {
            this.grupos.forEach(i -> i.setEmpresa(null));
        }
        if (grupos != null) {
            grupos.forEach(i -> i.setEmpresa(this));
        }
        this.grupos = grupos;
    }

    public Empresa grupos(Set<Grupo> grupos) {
        this.setGrupos(grupos);
        return this;
    }

    public Empresa addGrupo(Grupo grupo) {
        this.grupos.add(grupo);
        grupo.setEmpresa(this);
        return this;
    }

    public Empresa removeGrupo(Grupo grupo) {
        this.grupos.remove(grupo);
        grupo.setEmpresa(null);
        return this;
    }

    public Set<App> getApps() {
        return this.apps;
    }

    public void setApps(Set<App> apps) {
        if (this.apps != null) {
            this.apps.forEach(i -> i.setEmpresa(null));
        }
        if (apps != null) {
            apps.forEach(i -> i.setEmpresa(this));
        }
        this.apps = apps;
    }

    public Empresa apps(Set<App> apps) {
        this.setApps(apps);
        return this;
    }

    public Empresa addApp(App app) {
        this.apps.add(app);
        app.setEmpresa(this);
        return this;
    }

    public Empresa removeApp(App app) {
        this.apps.remove(app);
        app.setEmpresa(null);
        return this;
    }

    public Set<AppEmpresa> getAppEmpresas() {
        return this.appEmpresas;
    }

    public void setAppEmpresas(Set<AppEmpresa> appEmpresas) {
        if (this.appEmpresas != null) {
            this.appEmpresas.forEach(i -> i.setEmpresa(null));
        }
        if (appEmpresas != null) {
            appEmpresas.forEach(i -> i.setEmpresa(this));
        }
        this.appEmpresas = appEmpresas;
    }

    public Empresa appEmpresas(Set<AppEmpresa> appEmpresas) {
        this.setAppEmpresas(appEmpresas);
        return this;
    }

    public Empresa addAppEmpresa(AppEmpresa appEmpresa) {
        this.appEmpresas.add(appEmpresa);
        appEmpresa.setEmpresa(this);
        return this;
    }

    public Empresa removeAppEmpresa(AppEmpresa appEmpresa) {
        this.appEmpresas.remove(appEmpresa);
        appEmpresa.setEmpresa(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empresa)) {
            return false;
        }
        return id != null && id.equals(((Empresa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", nVarDescricao='" + getnVarDescricao() + "'" +
            "}";
    }
}
