import { IPapel } from 'app/entities/papel/papel.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IUsuarioPapel {
  id?: number;
  idnVarUsuarioCadastrado?: string;
  idnVarPapel?: string;
  idnVarUsuario?: string;
  papel?: IPapel | null;
  usuario?: IUsuario | null;
}

export class UsuarioPapel implements IUsuarioPapel {
  constructor(
    public id?: number,
    public idnVarUsuarioCadastrado?: string,
    public idnVarPapel?: string,
    public idnVarUsuario?: string,
    public papel?: IPapel | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getUsuarioPapelIdentifier(usuarioPapel: IUsuarioPapel): number | undefined {
  return usuarioPapel.id;
}
