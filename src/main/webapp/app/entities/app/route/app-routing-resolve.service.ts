import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApp, App } from '../app.model';
import { AppService } from '../service/app.service';

@Injectable({ providedIn: 'root' })
export class AppRoutingResolveService implements Resolve<IApp> {
  constructor(protected service: AppService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApp> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((app: HttpResponse<App>) => {
          if (app.body) {
            return of(app.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new App());
  }
}
