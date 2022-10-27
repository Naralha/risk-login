package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Grupo} entity. This class is used
 * in {@link io.sld.riskcomplianceloginservice.web.rest.GrupoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /grupos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GrupoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarGrupo;

    private StringFilter nVarNome;

    private StringFilter idnVarUsuario;

    private StringFilter idnVarEmpresa;

    private LongFilter grupoPapelId;

    private LongFilter usuarioGrupoId;

    private LongFilter empresaId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public GrupoCriteria() {}

    public GrupoCriteria(GrupoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarGrupo = other.idnVarGrupo == null ? null : other.idnVarGrupo.copy();
        this.nVarNome = other.nVarNome == null ? null : other.nVarNome.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.idnVarEmpresa = other.idnVarEmpresa == null ? null : other.idnVarEmpresa.copy();
        this.grupoPapelId = other.grupoPapelId == null ? null : other.grupoPapelId.copy();
        this.usuarioGrupoId = other.usuarioGrupoId == null ? null : other.usuarioGrupoId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GrupoCriteria copy() {
        return new GrupoCriteria(this);
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

    public LongFilter getUsuarioGrupoId() {
        return usuarioGrupoId;
    }

    public LongFilter usuarioGrupoId() {
        if (usuarioGrupoId == null) {
            usuarioGrupoId = new LongFilter();
        }
        return usuarioGrupoId;
    }

    public void setUsuarioGrupoId(LongFilter usuarioGrupoId) {
        this.usuarioGrupoId = usuarioGrupoId;
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
        final GrupoCriteria that = (GrupoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarGrupo, that.idnVarGrupo) &&
            Objects.equals(nVarNome, that.nVarNome) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(idnVarEmpresa, that.idnVarEmpresa) &&
            Objects.equals(grupoPapelId, that.grupoPapelId) &&
            Objects.equals(usuarioGrupoId, that.usuarioGrupoId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            idnVarGrupo,
            nVarNome,
            idnVarUsuario,
            idnVarEmpresa,
            grupoPapelId,
            usuarioGrupoId,
            empresaId,
            usuarioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GrupoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarGrupo != null ? "idnVarGrupo=" + idnVarGrupo + ", " : "") +
            (nVarNome != null ? "nVarNome=" + nVarNome + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (idnVarEmpresa != null ? "idnVarEmpresa=" + idnVarEmpresa + ", " : "") +
            (grupoPapelId != null ? "grupoPapelId=" + grupoPapelId + ", " : "") +
            (usuarioGrupoId != null ? "usuarioGrupoId=" + usuarioGrupoId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
