import { IApp } from 'app/entities/app/app.model';
import { IAppEmpresa } from 'app/entities/app-empresa/app-empresa.model';
import { IFeatures } from 'app/entities/features/features.model';
import { IGrupo } from 'app/entities/grupo/grupo.model';
import { IGrupoPapel } from 'app/entities/grupo-papel/grupo-papel.model';
import { IPermissions } from 'app/entities/permissions/permissions.model';
import { IPermissionsPapel } from 'app/entities/permissions-papel/permissions-papel.model';
import { IPapel } from 'app/entities/papel/papel.model';
import { IUsuarioGrupo } from 'app/entities/usuario-grupo/usuario-grupo.model';
import { IUsuarioPapel } from 'app/entities/usuario-papel/usuario-papel.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IUsuario {
  id?: number;
  idnVarUsuario?: string;
  nVarNome?: string;
  idnVarEmpresa?: string | null;
  idnVarUsuarioCadastro?: string | null;
  nVarSenha?: string;
  apps?: IApp[] | null;
  appEmpresas?: IAppEmpresa[] | null;
  features?: IFeatures[] | null;
  grupos?: IGrupo[] | null;
  grupoPapels?: IGrupoPapel[] | null;
  permissions?: IPermissions[] | null;
  permissionsPapels?: IPermissionsPapel[] | null;
  papels?: IPapel[] | null;
  usuarioGrupos?: IUsuarioGrupo[] | null;
  usuarioPapels?: IUsuarioPapel[] | null;
  empresa?: IEmpresa | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public idnVarUsuario?: string,
    public nVarNome?: string,
    public idnVarEmpresa?: string | null,
    public idnVarUsuarioCadastro?: string | null,
    public nVarSenha?: string,
    public apps?: IApp[] | null,
    public appEmpresas?: IAppEmpresa[] | null,
    public features?: IFeatures[] | null,
    public grupos?: IGrupo[] | null,
    public grupoPapels?: IGrupoPapel[] | null,
    public permissions?: IPermissions[] | null,
    public permissionsPapels?: IPermissionsPapel[] | null,
    public papels?: IPapel[] | null,
    public usuarioGrupos?: IUsuarioGrupo[] | null,
    public usuarioPapels?: IUsuarioPapel[] | null,
    public empresa?: IEmpresa | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
