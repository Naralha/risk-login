package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.resource.UsuarioGrupoResource;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link UsuarioGrupo} entity. This class is used
 * in {@link UsuarioGrupoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuario-grupos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioGrupoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarUsuarioCadastrado;

    private StringFilter idnVarGrupo;

    private StringFilter idnVarUsuario;

    private LongFilter grupoId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public UsuarioGrupoCriteria() {}

    public UsuarioGrupoCriteria(UsuarioGrupoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarUsuarioCadastrado = other.idnVarUsuarioCadastrado == null ? null : other.idnVarUsuarioCadastrado.copy();
        this.idnVarGrupo = other.idnVarGrupo == null ? null : other.idnVarGrupo.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.grupoId = other.grupoId == null ? null : other.grupoId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioGrupoCriteria copy() {
        return new UsuarioGrupoCriteria(this);
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

    public StringFilter getIdnVarUsuarioCadastrado() {
        return idnVarUsuarioCadastrado;
    }

    public StringFilter idnVarUsuarioCadastrado() {
        if (idnVarUsuarioCadastrado == null) {
            idnVarUsuarioCadastrado = new StringFilter();
        }
        return idnVarUsuarioCadastrado;
    }

    public void setIdnVarUsuarioCadastrado(StringFilter idnVarUsuarioCadastrado) {
        this.idnVarUsuarioCadastrado = idnVarUsuarioCadastrado;
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
        final UsuarioGrupoCriteria that = (UsuarioGrupoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarUsuarioCadastrado, that.idnVarUsuarioCadastrado) &&
            Objects.equals(idnVarGrupo, that.idnVarGrupo) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(grupoId, that.grupoId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarUsuarioCadastrado, idnVarGrupo, idnVarUsuario, grupoId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioGrupoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarUsuarioCadastrado != null ? "idnVarUsuarioCadastrado=" + idnVarUsuarioCadastrado + ", " : "") +
            (idnVarGrupo != null ? "idnVarGrupo=" + idnVarGrupo + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (grupoId != null ? "grupoId=" + grupoId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
