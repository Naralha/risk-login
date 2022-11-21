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
 * A Features.
 */
@Entity
@Table(name = "features")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Features implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idn_var_features", nullable = false)
    private String idnVarFeatures;

    @NotNull
    @Column(name = "n_var_nome", nullable = false)
    private String nVarNome;

    @NotNull
    @Column(name = "idn_var_app", nullable = false)
    private String idnVarApp;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @OneToMany(mappedBy = "features")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "papel", "permissions", "features", "usuario" }, allowSetters = true)
    private Set<PermissionsPapel> permissionsPapels = new HashSet<>();

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

    public Features id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarFeatures() {
        return this.idnVarFeatures;
    }

    public Features idnVarFeatures(String idnVarFeatures) {
        this.setIdnVarFeatures(idnVarFeatures);
        return this;
    }

    public void setIdnVarFeatures(String idnVarFeatures) {
        this.idnVarFeatures = idnVarFeatures;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public Features nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getIdnVarApp() {
        return this.idnVarApp;
    }

    public Features idnVarApp(String idnVarApp) {
        this.setIdnVarApp(idnVarApp);
        return this;
    }

    public void setIdnVarApp(String idnVarApp) {
        this.idnVarApp = idnVarApp;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public Features idnVarUsuario(String idnVarUsuario) {
        this.setIdnVarUsuario(idnVarUsuario);
        return this;
    }

    public void setIdnVarUsuario(String idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public Set<PermissionsPapel> getPermissionsPapels() {
        return this.permissionsPapels;
    }

    public void setPermissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        if (this.permissionsPapels != null) {
            this.permissionsPapels.forEach(i -> i.setFeatures(null));
        }
        if (permissionsPapels != null) {
            permissionsPapels.forEach(i -> i.setFeatures(this));
        }
        this.permissionsPapels = permissionsPapels;
    }

    public Features permissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        this.setPermissionsPapels(permissionsPapels);
        return this;
    }

    public Features addPermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.add(permissionsPapel);
        permissionsPapel.setFeatures(this);
        return this;
    }

    public Features removePermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.remove(permissionsPapel);
        permissionsPapel.setFeatures(null);
        return this;
    }

    public App getApp() {
        return this.app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Features app(App app) {
        this.setApp(app);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Features usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Features)) {
            return false;
        }
        return id != null && id.equals(((Features) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Features{" +
            "id=" + getId() +
            ", idnVarFeatures='" + getIdnVarFeatures() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", idnVarApp='" + getIdnVarApp() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            "}";
    }
}
