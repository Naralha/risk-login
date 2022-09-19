import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGrupoPapel, GrupoPapel } from '../grupo-papel.model';
import { GrupoPapelService } from '../service/grupo-papel.service';

@Injectable({ providedIn: 'root' })
export class GrupoPapelRoutingResolveService implements Resolve<IGrupoPapel> {
  constructor(protected service: GrupoPapelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGrupoPapel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((grupoPapel: HttpResponse<GrupoPapel>) => {
          if (grupoPapel.body) {
            return of(grupoPapel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GrupoPapel());
  }
}
