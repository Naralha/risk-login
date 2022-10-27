package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Empresa} entity. This class is used
 * in {@link io.sld.riskcomplianceloginservice.web.rest.EmpresaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empresas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpresaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarEmpresa;

    private StringFilter nVarNome;

    private StringFilter nVarDescricao;

    private LongFilter grupoPapelId;

    private LongFilter usuarioId;

    private LongFilter grupoId;

    private LongFilter appId;

    private LongFilter appEmpresaId;

    private Boolean distinct;

    public EmpresaCriteria() {}

    public EmpresaCriteria(EmpresaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarEmpresa = other.idnVarEmpresa == null ? null : other.idnVarEmpresa.copy();
        this.nVarNome = other.nVarNome == null ? null : other.nVarNome.copy();
        this.nVarDescricao = other.nVarDescricao == null ? null : other.nVarDescricao.copy();
        this.grupoPapelId = other.grupoPapelId == null ? null : other.grupoPapelId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.grupoId = other.grupoId == null ? null : other.grupoId.copy();
        this.appId = other.appId == null ? null : other.appId.copy();
        this.appEmpresaId = other.appEmpresaId == null ? null : other.appEmpresaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmpresaCriteria copy() {
        return new EmpresaCriteria(this);
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

    public StringFilter getnVarDescricao() {
        return nVarDescricao;
    }

    public StringFilter nVarDescricao() {
        if (nVarDescricao == null) {
            nVarDescricao = new StringFilter();
        }
        return nVarDescricao;
    }

    public void setnVarDescricao(StringFilter nVarDescricao) {
        this.nVarDescricao = nVarDescricao;
    }

    public LongFilter getGrupoPapelId() {
        return grupoPapelId;
    }

    public LongFilter grupoPapelId() {
        if (grupoPapelId == null) {
            grupoPapelId = new LongFilter();
        }
        return grupoPapelId;
    }

    public void setGrupoPapelId(LongFilter grupoPapelId) {
        this.grupoPapelId = grupoPapelId;
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

    public LongFilter getGrupoId() {
        return grupoId;
    }

    public LongFilter grupoId() {
        if (grupoId == null) {
            grupoId = new LongFilter();
        }
        return grupoId;
    }

    public void setGrupoId(LongFilter grupoId) {
        this.grupoId = grupoId;
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
        final EmpresaCriteria that = (EmpresaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarEmpresa, that.idnVarEmpresa) &&
            Objects.equals(nVarNome, that.nVarNome) &&
            Objects.equals(nVarDescricao, that.nVarDescricao) &&
            Objects.equals(grupoPapelId, that.grupoPapelId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(grupoId, that.grupoId) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(appEmpresaId, that.appEmpresaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarEmpresa, nVarNome, nVarDescricao, grupoPapelId, usuarioId, grupoId, appId, appEmpresaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarEmpresa != null ? "idnVarEmpresa=" + idnVarEmpresa + ", " : "") +
            (nVarNome != null ? "nVarNome=" + nVarNome + ", " : "") +
            (nVarDescricao != null ? "nVarDescricao=" + nVarDescricao + ", " : "") +
            (grupoPapelId != null ? "grupoPapelId=" + grupoPapelId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (grupoId != null ? "grupoId=" + grupoId + ", " : "") +
            (appId != null ? "appId=" + appId + ", " : "") +
            (appEmpresaId != null ? "appEmpresaId=" + appEmpresaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
