package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Permissions} entity. This class is used
 * in {@link io.sld.riskcomplianceloginservice.web.rest.PermissionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarPermissions;

    private StringFilter nVarNome;

    private StringFilter nVarTipoPermissao;

    private StringFilter idnVarUsuario;

    private LongFilter permissionsPapelId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public PermissionsCriteria() {}

    public PermissionsCriteria(PermissionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarPermissions = other.idnVarPermissions == null ? null : other.idnVarPermissions.copy();
        this.nVarNome = other.nVarNome == null ? null : other.nVarNome.copy();
        this.nVarTipoPermissao = other.nVarTipoPermissao == null ? null : other.nVarTipoPermissao.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.permissionsPapelId = other.permissionsPapelId == null ? null : other.permissionsPapelId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PermissionsCriteria copy() {
        return new PermissionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIdnVarPermissions() {
        return idnVarPermissions;
    }

    public StringFilter idnVarPermissions() {
        if (idnVarPermissions == null) {
            idnVarPermissions = new StringFilter();
        }
        return idnVarPermissions;
    }

    public void setIdnVarPermissions(StringFilter idnVarPermissions) {
        this.idnVarPermissions = idnVarPermissions;
    }

    public StringFilter getnVarNome() {
        return nVarNome;
    }

    public StringFilter nVarNome() {
        if (nVarNome == null) {
            nVarNome = new StringFilter();
        }
        return nVarNome;
    }

    public void setnVarNome(StringFilter nVarNome) {
        this.nVarNome = nVarNome;
    }

    public StringFilter getnVarTipoPermissao() {
        return nVarTipoPermissao;
    }

    public StringFilter nVarTipoPermissao() {
        if (nVarTipoPermissao == null) {
            nVarTipoPermissao = new StringFilter();
        }
        return nVarTipoPermissao;
    }

    public void setnVarTipoPermissao(StringFilter nVarTipoPermissao) {
        this.nVarTipoPermissao = nVarTipoPermissao;
    }

    public StringFilter getIdnVarUsuario() {
        return idnVarUsuario;
    }

    public StringFilter idnVarUsuario() {
        if (idnVarUsuario == null) {
            idnVarUsuario = new StringFilter();
        }
        return idnVarUsuario;
    }

    public void setIdnVarUsuario(StringFilter idnVarUsuario) {
        this.idnVarUsuario = idnVarUsuario;
    }

    public LongFilter getPermissionsPapelId() {
        return permissionsPapelId;
    }

    public LongFilter permissionsPapelId() {
        if (permissionsPapelId == null) {
            permissionsPapelId = new LongFilter();
        }
        return permissionsPapelId;
    }

    public void setPermissionsPapelId(LongFilter permissionsPapelId) {
        this.permissionsPapelId = permissionsPapelId;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            usuarioId = new LongFilter();
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PermissionsCriteria that = (PermissionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarPermissions, that.idnVarPermissions) &&
            Objects.equals(nVarNome, that.nVarNome) &&
            Objects.equals(nVarTipoPermissao, that.nVarTipoPermissao) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(permissionsPapelId, that.permissionsPapelId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarPermissions, nVarNome, nVarTipoPermissao, idnVarUsuario, permissionsPapelId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarPermissions != null ? "idnVarPermissions=" + idnVarPermissions + ", " : "") +
            (nVarNome != null ? "nVarNome=" + nVarNome + ", " : "") +
            (nVarTipoPermissao != null ? "nVarTipoPermissao=" + nVarTipoPermissao + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (permissionsPapelId != null ? "permissionsPapelId=" + permissionsPapelId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
