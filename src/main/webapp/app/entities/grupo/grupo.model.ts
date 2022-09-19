import { IGrupoPapel } from 'app/entities/grupo-papel/grupo-papel.model';
import { IUsuarioGrupo } from 'app/entities/usuario-grupo/usuario-grupo.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IGrupo {
  id?: number;
  idnVarGrupo?: string;
  nVarNome?: string;
  idnVarUsuario?: string;
  idnVarEmpresa?: string;
  grupoPapels?: IGrupoPapel[] | null;
  usuarioGrupos?: IUsuarioGrupo[] | null;
  empresa?: IEmpresa | null;
  usuario?: IUsuario | null;
}

export class Grupo implements IGrupo {
  constructor(
    public id?: number,
    public idnVarGrupo?: string,
    public nVarNome?: string,
    public idnVarUsuario?: string,
    public idnVarEmpresa?: string,
    public grupoPapels?: IGrupoPapel[] | null,
    public usuarioGrupos?: IUsuarioGrupo[] | null,
    public empresa?: IEmpresa | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getGrupoIdentifier(grupo: IGrupo): number | undefined {
  return grupo.id;
}
