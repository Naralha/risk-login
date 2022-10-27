package io.sld.riskcomplianceloginservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PermissionsPapel.
 */
@Entity
@Table(name = "permissions_papel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionsPapel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idn_var_permissions", nullable = false)
    private String idnVarPermissions;

    @NotNull
    @Column(name = "idn_var_papel", nullable = false)
    private String idnVarPapel;

    @NotNull
    @Column(name = "idn_var_features", nullable = false)
    private String idnVarFeatures;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Papel papel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "permissionsPapels", "usuario" }, allowSetters = true)
    private Permissions permissions;

    @ManyToOne
    @JsonIgnoreProperties(value = { "permissionsPapels", "app", "usuario" }, allowSetters = true)
    private Features features;

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

    public PermissionsPapel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarPermissions() {
        return this.idnVarPermissions;
    }

    public PermissionsPapel idnVarPermissions(String idnVarPermissions) {
        this.setIdnVarPermissions(idnVarPermissions);
        return this;
    }

    public void setIdnVarPermissions(String idnVarPermissions) {
        this.idnVarPermissions = idnVarPermissions;
    }

    public String getIdnVarPapel() {
        return this.idnVarPapel;
    }

    public PermissionsPapel idnVarPapel(String idnVarPapel) {
        this.setIdnVarPapel(idnVarPapel);
        return this;
    }

    public void setIdnVarPapel(String idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public String getIdnVarFeatures() {
        return this.idnVarFeatures;
    }

    public PermissionsPapel idnVarFeatures(String idnVarFeatures) {
        this.setIdnVarFeatures(idnVarFeatures);
        return this;
    }

    public void setIdnVarFeatures(String idnVarFeatures) {
        this.idnVarFeatures = idnVarFeatures;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public PermissionsPapel idnVarUsuario(String idnVarUsuario) {
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

    public PermissionsPapel papel(Papel papel) {
        this.setPapel(papel);
        return this;
    }

    public Permissions getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public PermissionsPapel permissions(Permissions permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Features getFeatures() {
        return this.features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public PermissionsPapel features(Features features) {
        this.setFeatures(features);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PermissionsPapel usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionsPapel)) {
            return false;
        }
        return id != null && id.equals(((PermissionsPapel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionsPapel{" +
            "id=" + getId() +
            ", idnVarPermissions='" + getIdnVarPermissions() + "'" +
            ", idnVarPapel='" + getIdnVarPapel() + "'" +
            ", idnVarFeatures='" + getIdnVarFeatures() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            "}";
    }
}
