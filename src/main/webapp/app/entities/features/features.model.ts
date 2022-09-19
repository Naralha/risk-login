import { IPermissionsPapel } from 'app/entities/permissions-papel/permissions-papel.model';
import { IApp } from 'app/entities/app/app.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IFeatures {
  id?: number;
  idnVarFeatures?: string;
  nVarNome?: string;
  idnVarApp?: string;
  idnVarUsuario?: string;
  permissionsPapels?: IPermissionsPapel[] | null;
  app?: IApp | null;
  usuario?: IUsuario | null;
}

export class Features implements IFeatures {
  constructor(
    public id?: number,
    public idnVarFeatures?: string,
    public nVarNome?: string,
    public idnVarApp?: string,
    public idnVarUsuario?: string,
    public permissionsPapels?: IPermissionsPapel[] | null,
    public app?: IApp | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getFeaturesIdentifier(features: IFeatures): number | undefined {
  return features.id;
}
