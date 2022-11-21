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
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @NotNull
    @Column(name = "n_var_nome", nullable = false)
    private String nVarNome;

    @Column(name = "idn_var_empresa")
    private String idnVarEmpresa;

    @Column(name = "idn_var_usuario_cadastro")
    private String idnVarUsuarioCadastro;

    @NotNull
    @Column(name = "n_var_senha", nullable = false)
    private String nVarSenha;

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appEmpresas", "features", "papels", "empresa", "usuario" }, allowSetters = true)
    private Set<App> apps = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "app", "empresa", "usuario" }, allowSetters = true)
    private Set<AppEmpresa> appEmpresas = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "permissionsPapels", "app", "usuario" }, allowSetters = true)
    private Set<Features> features = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarioGrupos", "empresa", "usuario" }, allowSetters = true)
    private Set<Grupo> grupos = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupo", "papel", "empresa", "usuario" }, allowSetters = true)
    private Set<GrupoPapel> grupoPapels = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "permissionsPapels", "usuario" }, allowSetters = true)
    private Set<Permissions> permissions = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "papel", "permissions", "features", "usuario" }, allowSetters = true)
    private Set<PermissionsPapel> permissionsPapels = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Set<Papel> papels = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupo", "usuario" }, allowSetters = true)
    private Set<UsuarioGrupo> usuarioGrupos = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "papel", "usuario" }, allowSetters = true)
    private Set<UsuarioPapel> usuarioPapels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarios", "grupos", "apps", "appEmpresas" }, allowSetters = true)
    private Empresa empresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public Usuario idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public Usuario nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getIdnVarEmpresa() {
        return this.idnVarEmpresa;
    }

    public Usuario idnVarEmpresa(String idnVarEmpresa) {
        this.setIdnVarEmpresa(idnVarEmpresa);
        return this;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public String getIdnVarUsuarioCadastro() {
        return this.idnVarUsuarioCadastro;
    }

    public Usuario idnVarUsuarioCadastro(String idnVarUsuarioCadastro) {
        this.setIdnVarUsuarioCadastro(idnVarUsuarioCadastro);
        return this;
    }

    public void setIdnVarUsuarioCadastro(String idnVarUsuarioCadastro) {
        this.idnVarUsuarioCadastro = idnVarUsuarioCadastro;
    }

    public String getnVarSenha() {
        return this.nVarSenha;
    }

    public Usuario nVarSenha(String nVarSenha) {
        this.setnVarSenha(nVarSenha);
        return this;
    }

    public void setnVarSenha(String nVarSenha) {
        this.nVarSenha = nVarSenha;
    }

    public Set<App> getApps() {
        return this.apps;
    }

    public void setApps(Set<App> apps) {
        if (this.apps != null) {
            this.apps.forEach(i -> i.setUsuario(null));
        }
        if (apps != null) {
            apps.forEach(i -> i.setUsuario(this));
        }
        this.apps = apps;
    }

    public Usuario apps(Set<App> apps) {
        this.setApps(apps);
        return this;
    }

    public Usuario addApp(App app) {
        this.apps.add(app);
        app.setUsuario(this);
        return this;
    }

    public Usuario removeApp(App app) {
        this.apps.remove(app);
        app.setUsuario(null);
        return this;
    }

    public Set<AppEmpresa> getAppEmpresas() {
        return this.appEmpresas;
    }

    public void setAppEmpresas(Set<AppEmpresa> appEmpresas) {
        if (this.appEmpresas != null) {
            this.appEmpresas.forEach(i -> i.setUsuario(null));
        }
        if (appEmpresas != null) {
            appEmpresas.forEach(i -> i.setUsuario(this));
        }
        this.appEmpresas = appEmpresas;
    }

    public Usuario appEmpresas(Set<AppEmpresa> appEmpresas) {
        this.setAppEmpresas(appEmpresas);
        return this;
    }

    public Usuario addAppEmpresa(AppEmpresa appEmpresa) {
        this.appEmpresas.add(appEmpresa);
        appEmpresa.setUsuario(this);
        return this;
    }

    public Usuario removeAppEmpresa(AppEmpresa appEmpresa) {
        this.appEmpresas.remove(appEmpresa);
        appEmpresa.setUsuario(null);
        return this;
    }

    public Set<Features> getFeatures() {
        return this.features;
    }

    public void setFeatures(Set<Features> features) {
        if (this.features != null) {
            this.features.forEach(i -> i.setUsuario(null));
        }
        if (features != null) {
            features.forEach(i -> i.setUsuario(this));
        }
        this.features = features;
    }

    public Usuario features(Set<Features> features) {
        this.setFeatures(features);
        return this;
    }

    public Usuario addFeatures(Features features) {
        this.features.add(features);
        features.setUsuario(this);
        return this;
    }

    public Usuario removeFeatures(Features features) {
        this.features.remove(features);
        features.setUsuario(null);
        return this;
    }

    public Set<Grupo> getGrupos() {
        return this.grupos;
    }

    public void setGrupos(Set<Grupo> grupos) {
        if (this.grupos != null) {
            this.grupos.forEach(i -> i.setUsuario(null));
        }
        if (grupos != null) {
            grupos.forEach(i -> i.setUsuario(this));
        }
        this.grupos = grupos;
    }

    public Usuario grupos(Set<Grupo> grupos) {
        this.setGrupos(grupos);
        return this;
    }

    public Usuario addGrupo(Grupo grupo) {
        this.grupos.add(grupo);
        grupo.setUsuario(this);
        return this;
    }

    public Usuario removeGrupo(Grupo grupo) {
        this.grupos.remove(grupo);
        grupo.setUsuario(null);
        return this;
    }

    public Set<GrupoPapel> getGrupoPapels() {
        return this.grupoPapels;
    }

    public void setGrupoPapels(Set<GrupoPapel> grupoPapels) {
        if (this.grupoPapels != null) {
            this.grupoPapels.forEach(i -> i.setUsuario(null));
        }
        if (grupoPapels != null) {
            grupoPapels.forEach(i -> i.setUsuario(this));
        }
        this.grupoPapels = grupoPapels;
    }

    public Usuario grupoPapels(Set<GrupoPapel> grupoPapels) {
        this.setGrupoPapels(grupoPapels);
        return this;
    }

    public Usuario addGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.add(grupoPapel);
        grupoPapel.setUsuario(this);
        return this;
    }

    public Usuario removeGrupoPapel(GrupoPapel grupoPapel) {
        this.grupoPapels.remove(grupoPapel);
        grupoPapel.setUsuario(null);
        return this;
    }

    public Set<Permissions> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permissions> permissions) {
        if (this.permissions != null) {
            this.permissions.forEach(i -> i.setUsuario(null));
        }
        if (permissions != null) {
            permissions.forEach(i -> i.setUsuario(this));
        }
        this.permissions = permissions;
    }

    public Usuario permissions(Set<Permissions> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Usuario addPermissions(Permissions permissions) {
        this.permissions.add(permissions);
        permissions.setUsuario(this);
        return this;
    }

    public Usuario removePermissions(Permissions permissions) {
        this.permissions.remove(permissions);
        permissions.setUsuario(null);
        return this;
    }

    public Set<PermissionsPapel> getPermissionsPapels() {
        return this.permissionsPapels;
    }

    public void setPermissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        if (this.permissionsPapels != null) {
            this.permissionsPapels.forEach(i -> i.setUsuario(null));
        }
        if (permissionsPapels != null) {
            permissionsPapels.forEach(i -> i.setUsuario(this));
        }
        this.permissionsPapels = permissionsPapels;
    }

    public Usuario permissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        this.setPermissionsPapels(permissionsPapels);
        return this;
    }

    public Usuario addPermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.add(permissionsPapel);
        permissionsPapel.setUsuario(this);
        return this;
    }

    public Usuario removePermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.remove(permissionsPapel);
        permissionsPapel.setUsuario(null);
        return this;
    }

    public Set<Papel> getPapels() {
        return this.papels;
    }

    public void setPapels(Set<Papel> papels) {
        if (this.papels != null) {
            this.papels.forEach(i -> i.setUsuario(null));
        }
        if (papels != null) {
            papels.forEach(i -> i.setUsuario(this));
        }
        this.papels = papels;
    }

    public Usuario papels(Set<Papel> papels) {
        this.setPapels(papels);
        return this;
    }

    public Usuario addPapel(Papel papel) {
        this.papels.add(papel);
        papel.setUsuario(this);
        return this;
    }

    public Usuario removePapel(Papel papel) {
        this.papels.remove(papel);
        papel.setUsuario(null);
        return this;
    }

    public Set<UsuarioGrupo> getUsuarioGrupos() {
        return this.usuarioGrupos;
    }

    public void setUsuarioGrupos(Set<UsuarioGrupo> usuarioGrupos) {
        if (this.usuarioGrupos != null) {
            this.usuarioGrupos.forEach(i -> i.setUsuario(null));
        }
        if (usuarioGrupos != null) {
            usuarioGrupos.forEach(i -> i.setUsuario(this));
        }
        this.usuarioGrupos = usuarioGrupos;
    }

    public Usuario usuarioGrupos(Set<UsuarioGrupo> usuarioGrupos) {
        this.setUsuarioGrupos(usuarioGrupos);
        return this;
    }

    public Usuario addUsuarioGrupo(UsuarioGrupo usuarioGrupo) {
        this.usuarioGrupos.add(usuarioGrupo);
        usuarioGrupo.setUsuario(this);
        return this;
    }

    public Usuario removeUsuarioGrupo(UsuarioGrupo usuarioGrupo) {
        this.usuarioGrupos.remove(usuarioGrupo);
        usuarioGrupo.setUsuario(null);
        return this;
    }

    public Set<UsuarioPapel> getUsuarioPapels() {
        return this.usuarioPapels;
    }

    public void setUsuarioPapels(Set<UsuarioPapel> usuarioPapels) {
        if (this.usuarioPapels != null) {
            this.usuarioPapels.forEach(i -> i.setUsuario(null));
        }
        if (usuarioPapels != null) {
            usuarioPapels.forEach(i -> i.setUsuario(this));
        }
        this.usuarioPapels = usuarioPapels;
    }

    public Usuario usuarioPapels(Set<UsuarioPapel> usuarioPapels) {
        this.setUsuarioPapels(usuarioPapels);
        return this;
    }

    public Usuario addUsuarioPapel(UsuarioPapel usuarioPapel) {
        this.usuarioPapels.add(usuarioPapel);
        usuarioPapel.setUsuario(this);
        return this;
    }

    public Usuario removeUsuarioPapel(UsuarioPapel usuarioPapel) {
        this.usuarioPapels.remove(usuarioPapel);
        usuarioPapel.setUsuario(null);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Usuario empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            ", idnVarUsuarioCadastro='" + getIdnVarUsuarioCadastro() + "'" +
            ", nVarSenha='" + getnVarSenha() + "'" +
            "}";
    }
}
