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
 * A Permissions.
 */
@Entity
@Table(name = "permissions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Permissions implements Serializable {

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
    @Column(name = "n_var_nome", nullable = false)
    private String nVarNome;

    @NotNull
    @Column(name = "n_var_tipo_permissao", nullable = false)
    private String nVarTipoPermissao;

    @NotNull
    @Column(name = "idn_var_usuario", nullable = false)
    private String idnVarUsuario;

    @OneToMany(mappedBy = "permissions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "papel", "permissions", "features", "usuario" }, allowSetters = true)
    private Set<PermissionsPapel> permissionsPapels = new HashSet<>();

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

    public Permissions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdnVarPermissions() {
        return this.idnVarPermissions;
    }

    public Permissions idnVarPermissions(String idnVarPermissions) {
        this.setIdnVarPermissions(idnVarPermissions);
        return this;
    }

    public void setIdnVarPermissions(String idnVarPermissions) {
        this.idnVarPermissions = idnVarPermissions;
    }

    public String getnVarNome() {
        return this.nVarNome;
    }

    public Permissions nVarNome(String nVarNome) {
        this.setnVarNome(nVarNome);
        return this;
    }

    public void setnVarNome(String nVarNome) {
        this.nVarNome = nVarNome;
    }

    public String getnVarTipoPermissao() {
        return this.nVarTipoPermissao;
    }

    public Permissions nVarTipoPermissao(String nVarTipoPermissao) {
        this.setnVarTipoPermissao(nVarTipoPermissao);
        return this;
    }

    public void setnVarTipoPermissao(String nVarTipoPermissao) {
        this.nVarTipoPermissao = nVarTipoPermissao;
    }

    public String getIdnVarUsuario() {
        return this.idnVarUsuario;
    }

    public Permissions idnVarUsuario(String idnVarUsuario) {
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
            this.permissionsPapels.forEach(i -> i.setPermissions(null));
        }
        if (permissionsPapels != null) {
            permissionsPapels.forEach(i -> i.setPermissions(this));
        }
        this.permissionsPapels = permissionsPapels;
    }

    public Permissions permissionsPapels(Set<PermissionsPapel> permissionsPapels) {
        this.setPermissionsPapels(permissionsPapels);
        return this;
    }

    public Permissions addPermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.add(permissionsPapel);
        permissionsPapel.setPermissions(this);
        return this;
    }

    public Permissions removePermissionsPapel(PermissionsPapel permissionsPapel) {
        this.permissionsPapels.remove(permissionsPapel);
        permissionsPapel.setPermissions(null);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Permissions usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permissions)) {
            return false;
        }
        return id != null && id.equals(((Permissions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Permissions{" +
            "id=" + getId() +
            ", idnVarPermissions='" + getIdnVarPermissions() + "'" +
            ", nVarNome='" + getnVarNome() + "'" +
            ", nVarTipoPermissao='" + getnVarTipoPermissao() + "'" +
            ", idnVarUsuario='" + getIdnVarUsuario() + "'" +
            "}";
    }
}
