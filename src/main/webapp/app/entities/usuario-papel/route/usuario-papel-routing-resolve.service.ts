import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioPapel, UsuarioPapel } from '../usuario-papel.model';
import { UsuarioPapelService } from '../service/usuario-papel.service';

@Injectable({ providedIn: 'root' })
export class UsuarioPapelRoutingResolveService implements Resolve<IUsuarioPapel> {
  constructor(protected service: UsuarioPapelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUsuarioPapel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((usuarioPapel: HttpResponse<UsuarioPapel>) => {
          if (usuarioPapel.body) {
            return of(usuarioPapel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UsuarioPapel());
  }
}
