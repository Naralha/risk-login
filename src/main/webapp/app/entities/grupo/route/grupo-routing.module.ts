import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GrupoComponent } from '../list/grupo.component';
import { GrupoDetailComponent } from '../detail/grupo-detail.component';
import { GrupoUpdateComponent } from '../update/grupo-update.component';
import { GrupoRoutingResolveService } from './grupo-routing-resolve.service';

const grupoRoute: Routes = [
  {
    path: '',
    component: GrupoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GrupoDetailComponent,
    resolve: {
      grupo: GrupoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GrupoUpdateComponent,
    resolve: {
      grupo: GrupoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GrupoUpdateComponent,
    resolve: {
      grupo: GrupoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(grupoRoute)],
  exports: [RouterModule],
})
export class GrupoRoutingModule {}
