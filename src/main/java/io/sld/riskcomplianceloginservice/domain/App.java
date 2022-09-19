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
 * A App.
 */
@Table("app")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("idn_var_app")
    private String idnVarApp;

    @NotNull(message = "must not be null")
    @Column("n_var_nome")
    private String nVarNome;

    @NotNull(message = "must not be null")
    @Column("idn_var_usuario")
    private String idnVarUsuario;

    @NotNull(message = "must not be null")
    @Column("idn_var_empresa")
    private String idnVarEmpresa;

    @Transient
    @JsonIgnoreProperties(value = { "app", "empresa", "usuario" }, allowSetters = true)
    private Set<AppEmpresa> appEmpresas = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "permissionsPapels", "app", "usuario" }, allowSetters = true)
    private Set<Features> features = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Set<Papel> papels = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "grupoPapels", "usuarios", "grupos", "apps", "appEmpresas" }, allowSetters = true)
    private Empresa empresa;

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

    @Column("empresa_id")
    private Long empresaId;

    @Column("usuario_id")
    private Long usuarioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public App id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarApp() {
        return this.idnVarApp;
    }

    public App idnVarApp(String idnVarApp) {
        this.setIdnVarApp(idnVarApp);
        return this;
    }

    public void setIdnVarApp(String idnVarApp) {
        this.idnVarApp = idnVarApp;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public App nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public App idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public String getIdnVarEmpresa() {
        return this.idnVarEmpresa;
    }

    public App idnVarEmpresa(String idnVarEmpresa) {
        this.setIdnVarEmpresa(idnVarEmpresa);
        return this;
    }

    public void setIdnVarEmpresa(String idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public Set<AppEmpresa> getAppEmpresas() {
        return this.appEmpresas;
    }

    public void setAppEmpresas(Set<AppEmpresa> appEmpresas) {
        if (this.appEmpresas != null) {
            this.appEmpresas.forEach(i -> i.setApp(null));
        }
        if (appEmpresas != null) {
            appEmpresas.forEach(i -> i.setApp(this));
        }
        this.appEmpresas = appEmpresas;
    }

    public App appEmpresas(Set<AppEmpresa> appEmpresas) {
        this.setAppEmpresas(appEmpresas);
        return this;
    }

    public App addAppEmpresa(AppEmpresa appEmpresa) {
        this.appEmpresas.add(appEmpresa);
        appEmpresa.setApp(this);
        return this;
    }

    public App removeAppEmpresa(AppEmpresa appEmpresa) {
        this.appEmpresas.remove(appEmpresa);
        appEmpresa.setApp(null);
        return this;
    }

    public Set<Features> getFeatures() {
        return this.features;
    }

    public void setFeatures(Set<Features> features) {
        if (this.features != null) {
            this.features.forEach(i -> i.setApp(null));
        }
        if (features != null) {
            features.forEach(i -> i.setApp(this));
        }
        this.features = features;
    }

    public App features(Set<Features> features) {
        this.setFeatures(features);
        return this;
    }

    public App addFeatures(Features features) {
        this.features.add(features);
        features.setApp(this);
        return this;
    }

    public App removeFeatures(Features features) {
        this.features.remove(features);
        features.setApp(null);
        return this;
    }

    public Set<Papel> getPapels() {
        return this.papels;
    }

    public void setPapels(Set<Papel> papels) {
        if (this.papels != null) {
            this.papels.forEach(i -> i.setApp(null));
        }
        if (papels != null) {
            papels.forEach(i -> i.setApp(this));
        }
        this.papels = papels;
    }

    public App papels(Set<Papel> papels) {
        this.setPapels(papels);
        return this;
    }

    public App addPapel(Papel papel) {
        this.papels.add(papel);
        papel.setApp(this);
        return this;
    }

    public App removePapel(Papel papel) {
        this.papels.remove(papel);
        papel.setApp(null);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        this.empresaId = empresa != null ? empresa.getId() : null;
    }

    public App empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario != null ? usuario.getId() : null;
    }

    public App usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Long getEmpresaId() {
        return this.empresaId;
    }

    public void setEmpresaId(Long empresa) {
        this.empresaId = empresa;
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
        if (!(o instanceof App)) {
            return false;
        }
        return id != null && id.equals(((App) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "App{" +
            "id=" + getId() +
            ", idnVarApp='" + getIdnVarApp() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            ", idnVarEmpresa='" + getIdnVarEmpresa() + "'" +
            "}";
    }
}
