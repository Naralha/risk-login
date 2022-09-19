import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GrupoPapelComponent } from '../list/grupo-papel.component';
import { GrupoPapelDetailComponent } from '../detail/grupo-papel-detail.component';
import { GrupoPapelUpdateComponent } from '../update/grupo-papel-update.component';
import { GrupoPapelRoutingResolveService } from './grupo-papel-routing-resolve.service';

const grupoPapelRoute: Routes = [
  {
    path: '',
    component: GrupoPapelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GrupoPapelDetailComponent,
    resolve: {
      grupoPapel: GrupoPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GrupoPapelUpdateComponent,
    resolve: {
      grupoPapel: GrupoPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GrupoPapelUpdateComponent,
    resolve: {
      grupoPapel: GrupoPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(grupoPapelRoute)],
  exports: [RouterModule],
})
export class GrupoPapelRoutingModule {}
