package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.domain.service.filter.LongFilter;
import io.sld.riskcomplianceloginservice.domain.service.filter.StringFilter;
import io.sld.riskcomplianceloginservice.resource.AppEmpresaResource;
import org.springdoc.api.annotations.ParameterObject;



@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppEmpresaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarApp;

    private StringFilter idnVarEmpresa;

    private StringFilter idnVarUsuario;

    private LongFilter appId;

    private LongFilter empresaId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public AppEmpresaCriteria() {}

    public AppEmpresaCriteria(AppEmpresaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarApp = other.idnVarApp == null ? null : other.idnVarApp.copy();
        this.idnVarEmpresa = other.idnVarEmpresa == null ? null : other.idnVarEmpresa.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.appId = other.appId == null ? null : other.appId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppEmpresaCriteria copy() {
        return new AppEmpresaCriteria(this);
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
        final AppEmpresaCriteria that = (AppEmpresaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarApp, that.idnVarApp) &&
            Objects.equals(idnVarEmpresa, that.idnVarEmpresa) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarApp, idnVarEmpresa, idnVarUsuario, appId, empresaId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppEmpresaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarApp != null ? "idnVarApp=" + idnVarApp + ", " : "") +
            (idnVarEmpresa != null ? "idnVarEmpresa=" + idnVarEmpresa + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (appId != null ? "appId=" + appId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
