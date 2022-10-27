package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.resource.PermissionsPapelResource;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link PermissionsPapel} entity. This class is used
 * in {@link PermissionsPapelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /permissions-papels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionsPapelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarPermissions;

    private StringFilter idnVarPapel;

    private StringFilter idnVarFeatures;

    private StringFilter idnVarUsuario;

    private LongFilter papelId;

    private LongFilter permissionsId;

    private LongFilter featuresId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public PermissionsPapelCriteria() {}

    public PermissionsPapelCriteria(PermissionsPapelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarPermissions = other.idnVarPermissions == null ? null : other.idnVarPermissions.copy();
        this.idnVarPapel = other.idnVarPapel == null ? null : other.idnVarPapel.copy();
        this.idnVarFeatures = other.idnVarFeatures == null ? null : other.idnVarFeatures.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.papelId = other.papelId == null ? null : other.papelId.copy();
        this.permissionsId = other.permissionsId == null ? null : other.permissionsId.copy();
        this.featuresId = other.featuresId == null ? null : other.featuresId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PermissionsPapelCriteria copy() {
        return new PermissionsPapelCriteria(this);
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

    public StringFilter getIdnVarPapel() {
        return idnVarPapel;
    }

    public StringFilter idnVarPapel() {
        if (idnVarPapel == null) {
            idnVarPapel = new StringFilter();
        }
        return idnVarPapel;
    }

    public void setIdnVarPapel(StringFilter idnVarPapel) {
        this.idnVarPapel = idnVarPapel;
    }

    public StringFilter getIdnVarFeatures() {
        return idnVarFeatures;
    }

    public StringFilter idnVarFeatures() {
        if (idnVarFeatures == null) {
            idnVarFeatures = new StringFilter();
        }
        return idnVarFeatures;
    }

    public void setIdnVarFeatures(StringFilter idnVarFeatures) {
        this.idnVarFeatures = idnVarFeatures;
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

    public LongFilter getPapelId() {
        return papelId;
    }

    public LongFilter papelId() {
        if (papelId == null) {
            papelId = new LongFilter();
        }
        return papelId;
    }

    public void setPapelId(LongFilter papelId) {
        this.papelId = papelId;
    }

    public LongFilter getPermissionsId() {
        return permissionsId;
    }

    public LongFilter permissionsId() {
        if (permissionsId == null) {
            permissionsId = new LongFilter();
        }
        return permissionsId;
    }

    public void setPermissionsId(LongFilter permissionsId) {
        this.permissionsId = permissionsId;
    }

    public LongFilter getFeaturesId() {
        return featuresId;
    }

    public LongFilter featuresId() {
        if (featuresId == null) {
            featuresId = new LongFilter();
        }
        return featuresId;
    }

    public void setFeaturesId(LongFilter featuresId) {
        this.featuresId = featuresId;
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
        final PermissionsPapelCriteria that = (PermissionsPapelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarPermissions, that.idnVarPermissions) &&
            Objects.equals(idnVarPapel, that.idnVarPapel) &&
            Objects.equals(idnVarFeatures, that.idnVarFeatures) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(papelId, that.papelId) &&
            Objects.equals(permissionsId, that.permissionsId) &&
            Objects.equals(featuresId, that.featuresId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            idnVarPermissions,
            idnVarPapel,
            idnVarFeatures,
            idnVarUsuario,
            papelId,
            permissionsId,
            featuresId,
            usuarioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionsPapelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarPermissions != null ? "idnVarPermissions=" + idnVarPermissions + ", " : "") +
            (idnVarPapel != null ? "idnVarPapel=" + idnVarPapel + ", " : "") +
            (idnVarFeatures != null ? "idnVarFeatures=" + idnVarFeatures + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (papelId != null ? "papelId=" + papelId + ", " : "") +
            (permissionsId != null ? "permissionsId=" + permissionsId + ", " : "") +
            (featuresId != null ? "featuresId=" + featuresId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
