import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppEmpresa, AppEmpresa } from '../app-empresa.model';
import { AppEmpresaService } from '../service/app-empresa.service';

@Injectable({ providedIn: 'root' })
export class AppEmpresaRoutingResolveService implements Resolve<IAppEmpresa> {
  constructor(protected service: AppEmpresaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppEmpresa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appEmpresa: HttpResponse<AppEmpresa>) => {
          if (appEmpresa.body) {
            return of(appEmpresa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppEmpresa());
  }
}
