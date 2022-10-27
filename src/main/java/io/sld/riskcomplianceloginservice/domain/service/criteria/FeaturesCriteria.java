package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.resource.FeaturesResource;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Features} entity. This class is used
 * in {@link FeaturesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /features?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeaturesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarFeatures;

    private StringFilter nVarNome;

    private StringFilter idnVarApp;

    private StringFilter idnVarUsuario;

    private LongFilter permissionsPapelId;

    private LongFilter appId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public FeaturesCriteria() {}

    public FeaturesCriteria(FeaturesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarFeatures = other.idnVarFeatures == null ? null : other.idnVarFeatures.copy();
        this.nVarNome = other.nVarNome == null ? null : other.nVarNome.copy();
        this.idnVarApp = other.idnVarApp == null ? null : other.idnVarApp.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.permissionsPapelId = other.permissionsPapelId == null ? null : other.permissionsPapelId.copy();
        this.appId = other.appId == null ? null : other.appId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FeaturesCriteria copy() {
        return new FeaturesCriteria(this);
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

    public StringFilter getIdnVarApp() {
        return idnVarApp;
    }

    public StringFilter idnVarApp() {
        if (idnVarApp == null) {
            idnVarApp = new StringFilter();
        }
        return idnVarApp;
    }

    public void setIdnVarApp(StringFilter idnVarApp) {
        this.idnVarApp = idnVarApp;
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

    public LongFilter getAppId() {
        return appId;
    }

    public LongFilter appId() {
        if (appId == null) {
            appId = new LongFilter();
        }
        return appId;
    }

    public void setAppId(LongFilter appId) {
        this.appId = appId;
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
        final FeaturesCriteria that = (FeaturesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarFeatures, that.idnVarFeatures) &&
            Objects.equals(nVarNome, that.nVarNome) &&
            Objects.equals(idnVarApp, that.idnVarApp) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(permissionsPapelId, that.permissionsPapelId) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarFeatures, nVarNome, idnVarApp, idnVarUsuario, permissionsPapelId, appId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeaturesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarFeatures != null ? "idnVarFeatures=" + idnVarFeatures + ", " : "") +
            (nVarNome != null ? "nVarNome=" + nVarNome + ", " : "") +
            (idnVarApp != null ? "idnVarApp=" + idnVarApp + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (permissionsPapelId != null ? "permissionsPapelId=" + permissionsPapelId + ", " : "") +
            (appId != null ? "appId=" + appId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
