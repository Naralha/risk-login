import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApp, getAppIdentifier } from '../app.model';

export type EntityResponseType = HttpResponse<IApp>;
export type EntityArrayResponseType = HttpResponse<IApp[]>;

@Injectable({ providedIn: 'root' })
export class AppService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(app: IApp): Observable<EntityResponseType> {
    return this.http.post<IApp>(this.resourceUrl, app, { observe: 'response' });
  }

  update(app: IApp): Observable<EntityResponseType> {
    return this.http.put<IApp>(`${this.resourceUrl}/${getAppIdentifier(app) as number}`, app, { observe: 'response' });
  }

  partialUpdate(app: IApp): Observable<EntityResponseType> {
    return this.http.patch<IApp>(`${this.resourceUrl}/${getAppIdentifier(app) as number}`, app, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppToCollectionIfMissing(appCollection: IApp[], ...appsToCheck: (IApp | null | undefined)[]): IApp[] {
    const apps: IApp[] = appsToCheck.filter(isPresent);
    if (apps.length > 0) {
      const appCollectionIdentifiers = appCollection.map(appItem => getAppIdentifier(appItem)!);
      const appsToAdd = apps.filter(appItem => {
        const appIdentifier = getAppIdentifier(appItem);
        if (appIdentifier == null || appCollectionIdentifiers.includes(appIdentifier)) {
          return false;
        }
        appCollectionIdentifiers.push(appIdentifier);
        return true;
      });
      return [...appsToAdd, ...appCollection];
    }
    return appCollection;
  }
}
