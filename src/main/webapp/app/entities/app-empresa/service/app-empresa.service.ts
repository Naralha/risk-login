import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppEmpresa, getAppEmpresaIdentifier } from '../app-empresa.model';

export type EntityResponseType = HttpResponse<IAppEmpresa>;
export type EntityArrayResponseType = HttpResponse<IAppEmpresa[]>;

@Injectable({ providedIn: 'root' })
export class AppEmpresaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-empresas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appEmpresa: IAppEmpresa): Observable<EntityResponseType> {
    return this.http.post<IAppEmpresa>(this.resourceUrl, appEmpresa, { observe: 'response' });
  }

  update(appEmpresa: IAppEmpresa): Observable<EntityResponseType> {
    return this.http.put<IAppEmpresa>(`${this.resourceUrl}/${getAppEmpresaIdentifier(appEmpresa) as number}`, appEmpresa, {
      observe: 'response',
    });
  }

  partialUpdate(appEmpresa: IAppEmpresa): Observable<EntityResponseType> {
    return this.http.patch<IAppEmpresa>(`${this.resourceUrl}/${getAppEmpresaIdentifier(appEmpresa) as number}`, appEmpresa, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppEmpresa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppEmpresa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppEmpresaToCollectionIfMissing(
    appEmpresaCollection: IAppEmpresa[],
    ...appEmpresasToCheck: (IAppEmpresa | null | undefined)[]
  ): IAppEmpresa[] {
    const appEmpresas: IAppEmpresa[] = appEmpresasToCheck.filter(isPresent);
    if (appEmpresas.length > 0) {
      const appEmpresaCollectionIdentifiers = appEmpresaCollection.map(appEmpresaItem => getAppEmpresaIdentifier(appEmpresaItem)!);
      const appEmpresasToAdd = appEmpresas.filter(appEmpresaItem => {
        const appEmpresaIdentifier = getAppEmpresaIdentifier(appEmpresaItem);
        if (appEmpresaIdentifier == null || appEmpresaCollectionIdentifiers.includes(appEmpresaIdentifier)) {
          return false;
        }
        appEmpresaCollectionIdentifiers.push(appEmpresaIdentifier);
        return true;
      });
      return [...appEmpresasToAdd, ...appEmpresaCollection];
    }
    return appEmpresaCollection;
  }
}
