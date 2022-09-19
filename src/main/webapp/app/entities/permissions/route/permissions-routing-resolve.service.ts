import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissions, Permissions } from '../permissions.model';
import { PermissionsService } from '../service/permissions.service';

@Injectable({ providedIn: 'root' })
export class PermissionsRoutingResolveService implements Resolve<IPermissions> {
  constructor(protected service: PermissionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermissions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permissions: HttpResponse<Permissions>) => {
          if (permissions.body) {
            return of(permissions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Permissions());
  }
}
