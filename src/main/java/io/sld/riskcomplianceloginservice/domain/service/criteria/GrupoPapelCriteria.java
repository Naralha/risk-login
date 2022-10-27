package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link GrupoPapel} entity. This class is used
 * in {@link io.sld.riskcomplianceloginservice.web.rest.GrupoPapelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /grupo-papels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GrupoPapelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarGrupo;

    private StringFilter idnVarPapel;

    private StringFilter idnVarUsuario;

    private StringFilter idnVarEmpresa;

    private LongFilter grupoId;

    private LongFilter papelId;

    private LongFilter empresaId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public GrupoPapelCriteria() {}

    public GrupoPapelCriteria(GrupoPapelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarGrupo = other.idnVarGrupo == null ? null : other.idnVarGrupo.copy();
        this.idnVarPapel = other.idnVarPapel == null ? null : other.idnVarPapel.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.idnVarEmpresa = other.idnVarEmpresa == null ? null : other.idnVarEmpresa.copy();
        this.grupoId = other.grupoId == null ? null : other.grupoId.copy();
        this.papelId = other.papelId == null ? null : other.papelId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GrupoPapelCriteria copy() {
        return new GrupoPapelCriteria(this);
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

    public StringFilter getIdnVarGrupo() {
        return idnVarGrupo;
    }

    public StringFilter idnVarGrupo() {
        if (idnVarGrupo == null) {
            idnVarGrupo = new StringFilter();
        }
        return idnVarGrupo;
    }

    public void setIdnVarGrupo(StringFilter idnVarGrupo) {
        this.idnVarGrupo = idnVarGrupo;
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
        final GrupoPapelCriteria that = (GrupoPapelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarGrupo, that.idnVarGrupo) &&
            Objects.equals(idnVarPapel, that.idnVarPapel) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(idnVarEmpresa, that.idnVarEmpresa) &&
            Objects.equals(grupoId, that.grupoId) &&
            Objects.equals(papelId, that.papelId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarGrupo, idnVarPapel, idnVarUsuario, idnVarEmpresa, grupoId, papelId, empresaId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GrupoPapelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarGrupo != null ? "idnVarGrupo=" + idnVarGrupo + ", " : "") +
            (idnVarPapel != null ? "idnVarPapel=" + idnVarPapel + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (idnVarEmpresa != null ? "idnVarEmpresa=" + idnVarEmpresa + ", " : "") +
            (grupoId != null ? "grupoId=" + grupoId + ", " : "") +
            (papelId != null ? "papelId=" + papelId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
