package io.sld.riskcomplianceloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PermissionsPapel.
 */
@Table("permissions_papel")
public class PermissionsPapel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("idn_var_permissions")
    private String idnVarPermissions;

    @NotNull(message = "must not be null")
    @Column("idn_var_papel")
    private String idnVarPapel;

    @NotNull(message = "must not be null")
    @Column("idn_var_features")
    private String idnVarFeatures;

    @NotNull(message = "must not be null")
    @Column("idn_var_usuario")
    private String idnVarUsuario;

    @Transient
    @JsonIgnoreProperties(value = { "grupoPapels", "permissionsPapels", "usuarioPapels", "app", "usuario" }, allowSetters = true)
    private Papel papel;

    @Transient
    @JsonIgnoreProperties(value = { "permissionsPapels", "usuario" }, allowSetters = true)
    private Permissions permissions;

    @Transient
    @JsonIgnoreProperties(value = { "permissionsPapels", "app", "usuario" }, allowSetters = true)
    private Features features;

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

    @Column("papel_id")
    private Long papelId;

    @Column("permissions_id")
    private Long permissionsId;

    @Column("features_id")
    private Long featuresId;

    @Column("usuario_id")
    private Long usuarioId;

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
        this.papelId = papel != null ? papel.getId() : null;
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
        this.permissionsId = permissions != null ? permissions.getId() : null;
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
        this.featuresId = features != null ? features.getId() : null;
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
        this.usuarioId = usuario != null ? usuario.getId() : null;
    }

    public PermissionsPapel usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Long getPapelId() {
        return this.papelId;
    }

    public void setPapelId(Long papel) {
        this.papelId = papel;
    }

    public Long getPermissionsId() {
        return this.permissionsId;
    }

    public void setPermissionsId(Long permissions) {
        this.permissionsId = permissions;
    }

    public Long getFeaturesId() {
        return this.featuresId;
    }

    public void setFeaturesId(Long features) {
        this.featuresId = features;
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
