package io.sld.riskcomplianceloginservice.domain.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Usuario} entity. This class is used
 * in {@link io.sld.riskcomplianceloginservice.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idnVarUsuario;

    private StringFilter nVarNome;

    private StringFilter idnVarEmpresa;

    private StringFilter idnVarUsuarioCadastro;

    private StringFilter nVarSenha;

    private LongFilter appId;

    private LongFilter appEmpresaId;

    private LongFilter featuresId;

    private LongFilter grupoId;

    private LongFilter grupoPapelId;

    private LongFilter permissionsId;

    private LongFilter permissionsPapelId;

    private LongFilter papelId;

    private LongFilter usuarioGrupoId;

    private LongFilter usuarioPapelId;

    private LongFilter empresaId;

    private Boolean distinct;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idnVarUsuario = other.idnVarUsuario == null ? null : other.idnVarUsuario.copy();
        this.nVarNome = other.nVarNome == null ? null : other.nVarNome.copy();
        this.idnVarEmpresa = other.idnVarEmpresa == null ? null : other.idnVarEmpresa.copy();
        this.idnVarUsuarioCadastro = other.idnVarUsuarioCadastro == null ? null : other.idnVarUsuarioCadastro.copy();
        this.nVarSenha = other.nVarSenha == null ? null : other.nVarSenha.copy();
        this.appId = other.appId == null ? null : other.appId.copy();
        this.appEmpresaId = other.appEmpresaId == null ? null : other.appEmpresaId.copy();
        this.featuresId = other.featuresId == null ? null : other.featuresId.copy();
        this.grupoId = other.grupoId == null ? null : other.grupoId.copy();
        this.grupoPapelId = other.grupoPapelId == null ? null : other.grupoPapelId.copy();
        this.permissionsId = other.permissionsId == null ? null : other.permissionsId.copy();
        this.permissionsPapelId = other.permissionsPapelId == null ? null : other.permissionsPapelId.copy();
        this.papelId = other.papelId == null ? null : other.papelId.copy();
        this.usuarioGrupoId = other.usuarioGrupoId == null ? null : other.usuarioGrupoId.copy();
        this.usuarioPapelId = other.usuarioPapelId == null ? null : other.usuarioPapelId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
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

    public StringFilter getIdnVarUsuarioCadastro() {
        return idnVarUsuarioCadastro;
    }

    public StringFilter idnVarUsuarioCadastro() {
        if (idnVarUsuarioCadastro == null) {
            idnVarUsuarioCadastro = new StringFilter();
        }
        return idnVarUsuarioCadastro;
    }

    public void setIdnVarUsuarioCadastro(StringFilter idnVarUsuarioCadastro) {
        this.idnVarUsuarioCadastro = idnVarUsuarioCadastro;
    }

    public StringFilter getnVarSenha() {
        return nVarSenha;
    }

    public StringFilter nVarSenha() {
        if (nVarSenha == null) {
            nVarSenha = new StringFilter();
        }
        return nVarSenha;
    }

    public void setnVarSenha(StringFilter nVarSenha) {
        this.nVarSenha = nVarSenha;
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

    public LongFilter getUsuarioPapelId() {
        return usuarioPapelId;
    }

    public LongFilter usuarioPapelId() {
        if (usuarioPapelId == null) {
            usuarioPapelId = new LongFilter();
        }
        return usuarioPapelId;
    }

    public void setUsuarioPapelId(LongFilter usuarioPapelId) {
        this.usuarioPapelId = usuarioPapelId;
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
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idnVarUsuario, that.idnVarUsuario) &&
            Objects.equals(nVarNome, that.nVarNome) &&
            Objects.equals(idnVarEmpresa, that.idnVarEmpresa) &&
            Objects.equals(idnVarUsuarioCadastro, that.idnVarUsuarioCadastro) &&
            Objects.equals(nVarSenha, that.nVarSenha) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(appEmpresaId, that.appEmpresaId) &&
            Objects.equals(featuresId, that.featuresId) &&
            Objects.equals(grupoId, that.grupoId) &&
            Objects.equals(grupoPapelId, that.grupoPapelId) &&
            Objects.equals(permissionsId, that.permissionsId) &&
            Objects.equals(permissionsPapelId, that.permissionsPapelId) &&
            Objects.equals(papelId, that.papelId) &&
            Objects.equals(usuarioGrupoId, that.usuarioGrupoId) &&
            Objects.equals(usuarioPapelId, that.usuarioPapelId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            idnVarUsuario,
            nVarNome,
            idnVarEmpresa,
            idnVarUsuarioCadastro,
            nVarSenha,
            appId,
            appEmpresaId,
            featuresId,
            grupoId,
            grupoPapelId,
            permissionsId,
            permissionsPapelId,
            papelId,
            usuarioGrupoId,
            usuarioPapelId,
            empresaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idnVarUsuario != null ? "idnVarUsuario=" + idnVarUsuario + ", " : "") +
            (nVarNome != null ? "nVarNome=" + nVarNome + ", " : "") +
            (idnVarEmpresa != null ? "idnVarEmpresa=" + idnVarEmpresa + ", " : "") +
            (idnVarUsuarioCadastro != null ? "idnVarUsuarioCadastro=" + idnVarUsuarioCadastro + ", " : "") +
            (nVarSenha != null ? "nVarSenha=" + nVarSenha + ", " : "") +
            (appId != null ? "appId=" + appId + ", " : "") +
            (appEmpresaId != null ? "appEmpresaId=" + appEmpresaId + ", " : "") +
            (featuresId != null ? "featuresId=" + featuresId + ", " : "") +
            (grupoId != null ? "grupoId=" + grupoId + ", " : "") +
            (grupoPapelId != null ? "grupoPapelId=" + grupoPapelId + ", " : "") +
            (permissionsId != null ? "permissionsId=" + permissionsId + ", " : "") +
            (permissionsPapelId != null ? "permissionsPapelId=" + permissionsPapelId + ", " : "") +
            (papelId != null ? "papelId=" + papelId + ", " : "") +
            (usuarioGrupoId != null ? "usuarioGrupoId=" + usuarioGrupoId + ", " : "") +
            (usuarioPapelId != null ? "usuarioPapelId=" + usuarioPapelId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
