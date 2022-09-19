import { IGrupoPapel } from 'app/entities/grupo-papel/grupo-papel.model';
import { IPermissionsPapel } from 'app/entities/permissions-papel/permissions-papel.model';
import { IUsuarioPapel } from 'app/entities/usuario-papel/usuario-papel.model';
import { IApp } from 'app/entities/app/app.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IPapel {
  id?: number;
  idnVarPapel?: string;
  nVarNome?: string;
  idnVarApp?: string;
  idnVarUsuario?: string;
  grupoPapels?: IGrupoPapel[] | null;
  permissionsPapels?: IPermissionsPapel[] | null;
  usuarioPapels?: IUsuarioPapel[] | null;
  app?: IApp | null;
  usuario?: IUsuario | null;
}

export class Papel implements IPapel {
  constructor(
    public id?: number,
    public idnVarPapel?: string,
    public nVarNome?: string,
    public idnVarApp?: string,
    public idnVarUsuario?: string,
    public grupoPapels?: IGrupoPapel[] | null,
    public permissionsPapels?: IPermissionsPapel[] | null,
    public usuarioPapels?: IUsuarioPapel[] | null,
    public app?: IApp | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getPapelIdentifier(papel: IPapel): number | undefined {
  return papel.id;
}
