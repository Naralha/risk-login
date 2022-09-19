import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermissionsPapel, getPermissionsPapelIdentifier } from '../permissions-papel.model';

export type EntityResponseType = HttpResponse<IPermissionsPapel>;
export type EntityArrayResponseType = HttpResponse<IPermissionsPapel[]>;

@Injectable({ providedIn: 'root' })
export class PermissionsPapelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permissions-papels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permissionsPapel: IPermissionsPapel): Observable<EntityResponseType> {
    return this.http.post<IPermissionsPapel>(this.resourceUrl, permissionsPapel, { observe: 'response' });
  }

  update(permissionsPapel: IPermissionsPapel): Observable<EntityResponseType> {
    return this.http.put<IPermissionsPapel>(
      `${this.resourceUrl}/${getPermissionsPapelIdentifier(permissionsPapel) as number}`,
      permissionsPapel,
      { observe: 'response' }
    );
  }

  partialUpdate(permissionsPapel: IPermissionsPapel): Observable<EntityResponseType> {
    return this.http.patch<IPermissionsPapel>(
      `${this.resourceUrl}/${getPermissionsPapelIdentifier(permissionsPapel) as number}`,
      permissionsPapel,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermissionsPapel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionsPapel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPermissionsPapelToCollectionIfMissing(
    permissionsPapelCollection: IPermissionsPapel[],
    ...permissionsPapelsToCheck: (IPermissionsPapel | null | undefined)[]
  ): IPermissionsPapel[] {
    const permissionsPapels: IPermissionsPapel[] = permissionsPapelsToCheck.filter(isPresent);
    if (permissionsPapels.length > 0) {
      const permissionsPapelCollectionIdentifiers = permissionsPapelCollection.map(
        permissionsPapelItem => getPermissionsPapelIdentifier(permissionsPapelItem)!
      );
      const permissionsPapelsToAdd = permissionsPapels.filter(permissionsPapelItem => {
        const permissionsPapelIdentifier = getPermissionsPapelIdentifier(permissionsPapelItem);
        if (permissionsPapelIdentifier == null || permissionsPapelCollectionIdentifiers.includes(permissionsPapelIdentifier)) {
          return false;
        }
        permissionsPapelCollectionIdentifiers.push(permissionsPapelIdentifier);
        return true;
      });
      return [...permissionsPapelsToAdd, ...permissionsPapelCollection];
    }
    return permissionsPapelCollection;
  }
}
