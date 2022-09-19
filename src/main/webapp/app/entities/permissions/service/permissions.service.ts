import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermissions, getPermissionsIdentifier } from '../permissions.model';

export type EntityResponseType = HttpResponse<IPermissions>;
export type EntityArrayResponseType = HttpResponse<IPermissions[]>;

@Injectable({ providedIn: 'root' })
export class PermissionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permissions: IPermissions): Observable<EntityResponseType> {
    return this.http.post<IPermissions>(this.resourceUrl, permissions, { observe: 'response' });
  }

  update(permissions: IPermissions): Observable<EntityResponseType> {
    return this.http.put<IPermissions>(`${this.resourceUrl}/${getPermissionsIdentifier(permissions) as number}`, permissions, {
      observe: 'response',
    });
  }

  partialUpdate(permissions: IPermissions): Observable<EntityResponseType> {
    return this.http.patch<IPermissions>(`${this.resourceUrl}/${getPermissionsIdentifier(permissions) as number}`, permissions, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermissions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissions[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPermissionsToCollectionIfMissing(
    permissionsCollection: IPermissions[],
    ...permissionsToCheck: (IPermissions | null | undefined)[]
  ): IPermissions[] {
    const permissions: IPermissions[] = permissionsToCheck.filter(isPresent);
    if (permissions.length > 0) {
      const permissionsCollectionIdentifiers = permissionsCollection.map(permissionsItem => getPermissionsIdentifier(permissionsItem)!);
      const permissionsToAdd = permissions.filter(permissionsItem => {
        const permissionsIdentifier = getPermissionsIdentifier(permissionsItem);
        if (permissionsIdentifier == null || permissionsCollectionIdentifiers.includes(permissionsIdentifier)) {
          return false;
        }
        permissionsCollectionIdentifiers.push(permissionsIdentifier);
        return true;
      });
      return [...permissionsToAdd, ...permissionsCollection];
    }
    return permissionsCollection;
  }
}
