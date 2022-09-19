import { IGrupo } from 'app/entities/grupo/grupo.model';
import { IPapel } from 'app/entities/papel/papel.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IGrupoPapel {
  id?: number;
  idnVarGrupo?: string;
  idnVarPapel?: string;
  idnVarUsuario?: string;
  idnVarEmpresa?: string;
  grupo?: IGrupo | null;
  papel?: IPapel | null;
  empresa?: IEmpresa | null;
  usuario?: IUsuario | null;
}

export class GrupoPapel implements IGrupoPapel {
  constructor(
    public id?: number,
    public idnVarGrupo?: string,
    public idnVarPapel?: string,
    public idnVarUsuario?: string,
    public idnVarEmpresa?: string,
    public grupo?: IGrupo | null,
    public papel?: IPapel | null,
    public empresa?: IEmpresa | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getGrupoPapelIdentifier(grupoPapel: IGrupoPapel): number | undefined {
  return grupoPapel.id;
}
