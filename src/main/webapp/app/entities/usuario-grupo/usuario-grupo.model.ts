import { IGrupo } from 'app/entities/grupo/grupo.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IUsuarioGrupo {
  id?: number;
  idnVarUsuarioCadastrado?: string;
  idnVarGrupo?: string;
  idnVarUsuario?: string;
  grupo?: IGrupo | null;
  usuario?: IUsuario | null;
}

export class UsuarioGrupo implements IUsuarioGrupo {
  constructor(
    public id?: number,
    public idnVarUsuarioCadastrado?: string,
    public idnVarGrupo?: string,
    public idnVarUsuario?: string,
    public grupo?: IGrupo | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getUsuarioGrupoIdentifier(usuarioGrupo: IUsuarioGrupo): number | undefined {
  return usuarioGrupo.id;
}
