import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPapel, getPapelIdentifier } from '../papel.model';

export type EntityResponseType = HttpResponse<IPapel>;
export type EntityArrayResponseType = HttpResponse<IPapel[]>;

@Injectable({ providedIn: 'root' })
export class PapelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/papels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(papel: IPapel): Observable<EntityResponseType> {
    return this.http.post<IPapel>(this.resourceUrl, papel, { observe: 'response' });
  }

  update(papel: IPapel): Observable<EntityResponseType> {
    return this.http.put<IPapel>(`${this.resourceUrl}/${getPapelIdentifier(papel) as number}`, papel, { observe: 'response' });
  }

  partialUpdate(papel: IPapel): Observable<EntityResponseType> {
    return this.http.patch<IPapel>(`${this.resourceUrl}/${getPapelIdentifier(papel) as number}`, papel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPapel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPapel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPapelToCollectionIfMissing(papelCollection: IPapel[], ...papelsToCheck: (IPapel | null | undefined)[]): IPapel[] {
    const papels: IPapel[] = papelsToCheck.filter(isPresent);
    if (papels.length > 0) {
      const papelCollectionIdentifiers = papelCollection.map(papelItem => getPapelIdentifier(papelItem)!);
      const papelsToAdd = papels.filter(papelItem => {
        const papelIdentifier = getPapelIdentifier(papelItem);
        if (papelIdentifier == null || papelCollectionIdentifiers.includes(papelIdentifier)) {
          return false;
        }
        papelCollectionIdentifiers.push(papelIdentifier);
        return true;
      });
      return [...papelsToAdd, ...papelCollection];
    }
    return papelCollection;
  }
}
