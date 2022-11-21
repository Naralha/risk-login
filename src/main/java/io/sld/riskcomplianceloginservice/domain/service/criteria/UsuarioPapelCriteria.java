package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.service.filter.LongFilter;
import io.sld.riskcomplianceloginservice.domain.service.filter.StringFilter;
import io.sld.riskcomplianceloginservice.resource.UsuarioPapelResource;
import org.springdoc.api.annotations.ParameterObject;



@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioPapelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarUsuarioCadastrado;

    private StringFilter idnVarPapel;

    private StringFilter idnVarUsuario;

    private LongFilter papelId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public UsuarioPapelCriteria() {}

    public UsuarioPapelCriteria(UsuarioPapelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarUsuarioCadastrado = other.idnVarUsuarioCadastrado == null ? null : other.idnVarUsuarioCadastrado.copy();
        this.idnVarPapel = other.idnVarPapel == null ? null : other.idnVarPapel.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.papelId = other.papelId == null ? null : other.papelId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioPapelCriteria copy() {
        return new UsuarioPapelCriteria(this);
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
        final UsuarioPapelCriteria that = (UsuarioPapelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarUsuarioCadastrado, that.idnVarUsuarioCadastrado) &&
            Objects.equals(idnVarPapel, that.idnVarPapel) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(papelId, that.papelId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idnVarUsuarioCadastrado, idnVarPapel, idnVarUsuario, papelId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioPapelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarUsuarioCadastrado != null ? "idnVarUsuarioCadastrado=" + idnVarUsuarioCadastrado + ", " : "") +
            (idnVarPapel != null ? "idnVarPapel=" + idnVarPapel + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (papelId != null ? "papelId=" + papelId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
