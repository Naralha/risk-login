import { IApp } from 'app/entities/app/app.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IAppEmpresa {
  id?: number;
  idnVarApp?: string;
  idnVarEmpresa?: string;
  idnVarUsuario?: string;
  app?: IApp | null;
  empresa?: IEmpresa | null;
  usuario?: IUsuario | null;
}

export class AppEmpresa implements IAppEmpresa {
  constructor(
    public id?: number,
    public idnVarApp?: string,
    public idnVarEmpresa?: string,
    public idnVarUsuario?: string,
    public app?: IApp | null,
    public empresa?: IEmpresa | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getAppEmpresaIdentifier(appEmpresa: IAppEmpresa): number | undefined {
  return appEmpresa.id;
}
