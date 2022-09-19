import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PapelComponent } from '../list/papel.component';
import { PapelDetailComponent } from '../detail/papel-detail.component';
import { PapelUpdateComponent } from '../update/papel-update.component';
import { PapelRoutingResolveService } from './papel-routing-resolve.service';

const papelRoute: Routes = [
  {
    path: '',
    component: PapelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PapelDetailComponent,
    resolve: {
      papel: PapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PapelUpdateComponent,
    resolve: {
      papel: PapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PapelUpdateComponent,
    resolve: {
      papel: PapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(papelRoute)],
  exports: [RouterModule],
})
export class PapelRoutingModule {}
