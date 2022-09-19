import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioGrupo, UsuarioGrupo } from '../usuario-grupo.model';
import { UsuarioGrupoService } from '../service/usuario-grupo.service';

@Injectable({ providedIn: 'root' })
export class UsuarioGrupoRoutingResolveService implements Resolve<IUsuarioGrupo> {
  constructor(protected service: UsuarioGrupoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUsuarioGrupo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((usuarioGrupo: HttpResponse<UsuarioGrupo>) => {
          if (usuarioGrupo.body) {
            return of(usuarioGrupo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UsuarioGrupo());
  }
}
