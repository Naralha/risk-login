import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarioPapel, getUsuarioPapelIdentifier } from '../usuario-papel.model';

export type EntityResponseType = HttpResponse<IUsuarioPapel>;
export type EntityArrayResponseType = HttpResponse<IUsuarioPapel[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioPapelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-papels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(usuarioPapel: IUsuarioPapel): Observable<EntityResponseType> {
    return this.http.post<IUsuarioPapel>(this.resourceUrl, usuarioPapel, { observe: 'response' });
  }

  update(usuarioPapel: IUsuarioPapel): Observable<EntityResponseType> {
    return this.http.put<IUsuarioPapel>(`${this.resourceUrl}/${getUsuarioPapelIdentifier(usuarioPapel) as number}`, usuarioPapel, {
      observe: 'response',
    });
  }

  partialUpdate(usuarioPapel: IUsuarioPapel): Observable<EntityResponseType> {
    return this.http.patch<IUsuarioPapel>(`${this.resourceUrl}/${getUsuarioPapelIdentifier(usuarioPapel) as number}`, usuarioPapel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUsuarioPapel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUsuarioPapel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsuarioPapelToCollectionIfMissing(
    usuarioPapelCollection: IUsuarioPapel[],
    ...usuarioPapelsToCheck: (IUsuarioPapel | null | undefined)[]
  ): IUsuarioPapel[] {
    const usuarioPapels: IUsuarioPapel[] = usuarioPapelsToCheck.filter(isPresent);
    if (usuarioPapels.length > 0) {
      const usuarioPapelCollectionIdentifiers = usuarioPapelCollection.map(
        usuarioPapelItem => getUsuarioPapelIdentifier(usuarioPapelItem)!
      );
      const usuarioPapelsToAdd = usuarioPapels.filter(usuarioPapelItem => {
        const usuarioPapelIdentifier = getUsuarioPapelIdentifier(usuarioPapelItem);
        if (usuarioPapelIdentifier == null || usuarioPapelCollectionIdentifiers.includes(usuarioPapelIdentifier)) {
          return false;
        }
        usuarioPapelCollectionIdentifiers.push(usuarioPapelIdentifier);
        return true;
      });
      return [...usuarioPapelsToAdd, ...usuarioPapelCollection];
    }
    return usuarioPapelCollection;
  }
}
