import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarioGrupo, getUsuarioGrupoIdentifier } from '../usuario-grupo.model';

export type EntityResponseType = HttpResponse<IUsuarioGrupo>;
export type EntityArrayResponseType = HttpResponse<IUsuarioGrupo[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioGrupoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-grupos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(usuarioGrupo: IUsuarioGrupo): Observable<EntityResponseType> {
    return this.http.post<IUsuarioGrupo>(this.resourceUrl, usuarioGrupo, { observe: 'response' });
  }

  update(usuarioGrupo: IUsuarioGrupo): Observable<EntityResponseType> {
    return this.http.put<IUsuarioGrupo>(`${this.resourceUrl}/${getUsuarioGrupoIdentifier(usuarioGrupo) as number}`, usuarioGrupo, {
      observe: 'response',
    });
  }

  partialUpdate(usuarioGrupo: IUsuarioGrupo): Observable<EntityResponseType> {
    return this.http.patch<IUsuarioGrupo>(`${this.resourceUrl}/${getUsuarioGrupoIdentifier(usuarioGrupo) as number}`, usuarioGrupo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUsuarioGrupo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUsuarioGrupo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsuarioGrupoToCollectionIfMissing(
    usuarioGrupoCollection: IUsuarioGrupo[],
    ...usuarioGruposToCheck: (IUsuarioGrupo | null | undefined)[]
  ): IUsuarioGrupo[] {
    const usuarioGrupos: IUsuarioGrupo[] = usuarioGruposToCheck.filter(isPresent);
    if (usuarioGrupos.length > 0) {
      const usuarioGrupoCollectionIdentifiers = usuarioGrupoCollection.map(
        usuarioGrupoItem => getUsuarioGrupoIdentifier(usuarioGrupoItem)!
      );
      const usuarioGruposToAdd = usuarioGrupos.filter(usuarioGrupoItem => {
        const usuarioGrupoIdentifier = getUsuarioGrupoIdentifier(usuarioGrupoItem);
        if (usuarioGrupoIdentifier == null || usuarioGrupoCollectionIdentifiers.includes(usuarioGrupoIdentifier)) {
          return false;
        }
        usuarioGrupoCollectionIdentifiers.push(usuarioGrupoIdentifier);
        return true;
      });
      return [...usuarioGruposToAdd, ...usuarioGrupoCollection];
    }
    return usuarioGrupoCollection;
  }
}
