package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link App} entity. This class is used
 * in {@link io.sld.riskcomplianceloginservice.web.rest.AppResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /apps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarApp;

    private StringFilter nVarNome;

    private StringFilter idnVarUsuario;

    private StringFilter idnVarEmpresa;

    private LongFilter appEmpresaId;

    private LongFilter featuresId;

    private LongFilter papelId;

    private LongFilter empresaId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public AppCriteria() {}

    public AppCriteria(AppCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarApp = other.idnVarApp == null ? null : other.idnVarApp.copy();
        this.nVarNome = other.nVarNome == null ? null : other.nVarNome.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.idnVarEmpresa = other.idnVarEmpresa == null ? null : other.idnVarEmpresa.copy();
        this.appEmpresaId = other.appEmpresaId == null ? null : other.appEmpresaId.copy();
        this.featuresId = other.featuresId == null ? null : other.featuresId.copy();
        this.papelId = other.papelId == null ? null : other.papelId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppCriteria copy() {
        return new AppCriteria(this);
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

    public StringFilter getIdnVarEmpresa() {
        return idnVarEmpresa;
    }

    public StringFilter idnVarEmpresa() {
        if (idnVarEmpresa == null) {
            idnVarEmpresa = new StringFilter();
        }
        return idnVarEmpresa;
    }

    public void setIdnVarEmpresa(StringFilter idnVarEmpresa) {
        this.idnVarEmpresa = idnVarEmpresa;
    }

    public LongFilter getAppEmpresaId() {
        return appEmpresaId;
    }

    public LongFilter appEmpresaId() {
        if (appEmpresaId == null) {
            appEmpresaId = new LongFilter();
        }
        return appEmpresaId;
    }

    public void setAppEmpresaId(LongFilter appEmpresaId) {
        this.appEmpresaId = appEmpresaId;
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

    public LongFilter getEmpresaId() {
        return empresaId;
    }

    public LongFilter empresaId() {
        if (empresaId == null) {
            empresaId = new LongFilter();
        }
        return empresaId;
    }

    public void setEmpresaId(LongFilter empresaId) {
        this.empresaId = empresaId;
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
        final AppCriteria that = (AppCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarApp, that.idnVarApp) &&
            Objects.equals(nVarNome, that.nVarNome) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(idnVarEmpresa, that.idnVarEmpresa) &&
            Objects.equals(appEmpresaId, that.appEmpresaId) &&
            Objects.equals(featuresId, that.featuresId) &&
            Objects.equals(papelId, that.papelId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            idnVarApp,
            nVarNome,
            idnVarUsuario,
            idnVarEmpresa,
            appEmpresaId,
            featuresId,
            papelId,
            empresaId,
            usuarioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarApp != null ? "idnVarApp=" + idnVarApp + ", " : "") +
            (nVarNome != null ? "nVarNome=" + nVarNome + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (idnVarEmpresa != null ? "idnVarEmpresa=" + idnVarEmpresa + ", " : "") +
            (appEmpresaId != null ? "appEmpresaId=" + appEmpresaId + ", " : "") +
            (featuresId != null ? "featuresId=" + featuresId + ", " : "") +
            (papelId != null ? "papelId=" + papelId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
