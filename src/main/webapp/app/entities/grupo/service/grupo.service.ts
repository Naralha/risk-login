import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGrupo, getGrupoIdentifier } from '../grupo.model';

export type EntityResponseType = HttpResponse<IGrupo>;
export type EntityArrayResponseType = HttpResponse<IGrupo[]>;

@Injectable({ providedIn: 'root' })
export class GrupoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/grupos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(grupo: IGrupo): Observable<EntityResponseType> {
    return this.http.post<IGrupo>(this.resourceUrl, grupo, { observe: 'response' });
  }

  update(grupo: IGrupo): Observable<EntityResponseType> {
    return this.http.put<IGrupo>(`${this.resourceUrl}/${getGrupoIdentifier(grupo) as number}`, grupo, { observe: 'response' });
  }

  partialUpdate(grupo: IGrupo): Observable<EntityResponseType> {
    return this.http.patch<IGrupo>(`${this.resourceUrl}/${getGrupoIdentifier(grupo) as number}`, grupo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGrupo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGrupo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGrupoToCollectionIfMissing(grupoCollection: IGrupo[], ...gruposToCheck: (IGrupo | null | undefined)[]): IGrupo[] {
    const grupos: IGrupo[] = gruposToCheck.filter(isPresent);
    if (grupos.length > 0) {
      const grupoCollectionIdentifiers = grupoCollection.map(grupoItem => getGrupoIdentifier(grupoItem)!);
      const gruposToAdd = grupos.filter(grupoItem => {
        const grupoIdentifier = getGrupoIdentifier(grupoItem);
        if (grupoIdentifier == null || grupoCollectionIdentifiers.includes(grupoIdentifier)) {
          return false;
        }
        grupoCollectionIdentifiers.push(grupoIdentifier);
        return true;
      });
      return [...gruposToAdd, ...grupoCollection];
    }
    return grupoCollection;
  }
}
