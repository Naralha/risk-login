import { IPapel } from 'app/entities/papel/papel.model';
import { IPermissions } from 'app/entities/permissions/permissions.model';
import { IFeatures } from 'app/entities/features/features.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IPermissionsPapel {
  id?: number;
  idnVarPermissions?: string;
  idnVarPapel?: string;
  idnVarFeatures?: string;
  idnVarUsuario?: string;
  papel?: IPapel | null;
  permissions?: IPermissions | null;
  features?: IFeatures | null;
  usuario?: IUsuario | null;
}

export class PermissionsPapel implements IPermissionsPapel {
  constructor(
    public id?: number,
    public idnVarPermissions?: string,
    public idnVarPapel?: string,
    public idnVarFeatures?: string,
    public idnVarUsuario?: string,
    public papel?: IPapel | null,
    public permissions?: IPermissions | null,
    public features?: IFeatures | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getPermissionsPapelIdentifier(permissionsPapel: IPermissionsPapel): number | undefined {
  return permissionsPapel.id;
}
