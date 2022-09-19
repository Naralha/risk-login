import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPapel, Papel } from '../papel.model';
import { PapelService } from '../service/papel.service';

@Injectable({ providedIn: 'root' })
export class PapelRoutingResolveService implements Resolve<IPapel> {
  constructor(protected service: PapelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPapel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((papel: HttpResponse<Papel>) => {
          if (papel.body) {
            return of(papel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Papel());
  }
}
