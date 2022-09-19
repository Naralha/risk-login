import { IPermissionsPapel } from 'app/entities/permissions-papel/permissions-papel.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IPermissions {
  id?: number;
  idnVarPermissions?: string;
  nVarNome?: string;
  nVarTipoPermissao?: string;
  idnVarUsuario?: string;
  permissionsPapels?: IPermissionsPapel[] | null;
  usuario?: IUsuario | null;
}

export class Permissions implements IPermissions {
  constructor(
    public id?: number,
    public idnVarPermissions?: string,
    public nVarNome?: string,
    public nVarTipoPermissao?: string,
    public idnVarUsuario?: string,
    public permissionsPapels?: IPermissionsPapel[] | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getPermissionsIdentifier(permissions: IPermissions): number | undefined {
  return permissions.id;
}
