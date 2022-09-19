import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGrupoPapel, getGrupoPapelIdentifier } from '../grupo-papel.model';

export type EntityResponseType = HttpResponse<IGrupoPapel>;
export type EntityArrayResponseType = HttpResponse<IGrupoPapel[]>;

@Injectable({ providedIn: 'root' })
export class GrupoPapelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/grupo-papels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(grupoPapel: IGrupoPapel): Observable<EntityResponseType> {
    return this.http.post<IGrupoPapel>(this.resourceUrl, grupoPapel, { observe: 'response' });
  }

  update(grupoPapel: IGrupoPapel): Observable<EntityResponseType> {
    return this.http.put<IGrupoPapel>(`${this.resourceUrl}/${getGrupoPapelIdentifier(grupoPapel) as number}`, grupoPapel, {
      observe: 'response',
    });
  }

  partialUpdate(grupoPapel: IGrupoPapel): Observable<EntityResponseType> {
    return this.http.patch<IGrupoPapel>(`${this.resourceUrl}/${getGrupoPapelIdentifier(grupoPapel) as number}`, grupoPapel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGrupoPapel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGrupoPapel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGrupoPapelToCollectionIfMissing(
    grupoPapelCollection: IGrupoPapel[],
    ...grupoPapelsToCheck: (IGrupoPapel | null | undefined)[]
  ): IGrupoPapel[] {
    const grupoPapels: IGrupoPapel[] = grupoPapelsToCheck.filter(isPresent);
    if (grupoPapels.length > 0) {
      const grupoPapelCollectionIdentifiers = grupoPapelCollection.map(grupoPapelItem => getGrupoPapelIdentifier(grupoPapelItem)!);
      const grupoPapelsToAdd = grupoPapels.filter(grupoPapelItem => {
        const grupoPapelIdentifier = getGrupoPapelIdentifier(grupoPapelItem);
        if (grupoPapelIdentifier == null || grupoPapelCollectionIdentifiers.includes(grupoPapelIdentifier)) {
          return false;
        }
        grupoPapelCollectionIdentifiers.push(grupoPapelIdentifier);
        return true;
      });
      return [...grupoPapelsToAdd, ...grupoPapelCollection];
    }
    return grupoPapelCollection;
  }
}
