import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissionsPapel, PermissionsPapel } from '../permissions-papel.model';
import { PermissionsPapelService } from '../service/permissions-papel.service';

@Injectable({ providedIn: 'root' })
export class PermissionsPapelRoutingResolveService implements Resolve<IPermissionsPapel> {
  constructor(protected service: PermissionsPapelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermissionsPapel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permissionsPapel: HttpResponse<PermissionsPapel>) => {
          if (permissionsPapel.body) {
            return of(permissionsPapel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PermissionsPapel());
  }
}
