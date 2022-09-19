import { IGrupoPapel } from 'app/entities/grupo-papel/grupo-papel.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IGrupo } from 'app/entities/grupo/grupo.model';
import { IApp } from 'app/entities/app/app.model';
import { IAppEmpresa } from 'app/entities/app-empresa/app-empresa.model';

export interface IEmpresa {
  id?: number;
  idnVarEmpresa?: string;
  nVarNome?: string;
  nVarDescricao?: string | null;
  grupoPapels?: IGrupoPapel[] | null;
  usuarios?: IUsuario[] | null;
  grupos?: IGrupo[] | null;
  apps?: IApp[] | null;
  appEmpresas?: IAppEmpresa[] | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public idnVarEmpresa?: string,
    public nVarNome?: string,
    public nVarDescricao?: string | null,
    public grupoPapels?: IGrupoPapel[] | null,
    public usuarios?: IUsuario[] | null,
    public grupos?: IGrupo[] | null,
    public apps?: IApp[] | null,
    public appEmpresas?: IAppEmpresa[] | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
