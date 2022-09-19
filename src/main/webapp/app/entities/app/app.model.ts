import { IAppEmpresa } from 'app/entities/app-empresa/app-empresa.model';
import { IFeatures } from 'app/entities/features/features.model';
import { IPapel } from 'app/entities/papel/papel.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IApp {
  id?: number;
  idnVarApp?: string;
  nVarNome?: string;
  idnVarUsuario?: string;
  idnVarEmpresa?: string;
  appEmpresas?: IAppEmpresa[] | null;
  features?: IFeatures[] | null;
  papels?: IPapel[] | null;
  empresa?: IEmpresa | null;
  usuario?: IUsuario | null;
}

export class App implements IApp {
  constructor(
    public id?: number,
    public idnVarApp?: string,
    public nVarNome?: string,
    public idnVarUsuario?: string,
    public idnVarEmpresa?: string,
    public appEmpresas?: IAppEmpresa[] | null,
    public features?: IFeatures[] | null,
    public papels?: IPapel[] | null,
    public empresa?: IEmpresa | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getAppIdentifier(app: IApp): number | undefined {
  return app.id;
}
