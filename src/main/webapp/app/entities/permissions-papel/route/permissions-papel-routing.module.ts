import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermissionsPapelComponent } from '../list/permissions-papel.component';
import { PermissionsPapelDetailComponent } from '../detail/permissions-papel-detail.component';
import { PermissionsPapelUpdateComponent } from '../update/permissions-papel-update.component';
import { PermissionsPapelRoutingResolveService } from './permissions-papel-routing-resolve.service';

const permissionsPapelRoute: Routes = [
  {
    path: '',
    component: PermissionsPapelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermissionsPapelDetailComponent,
    resolve: {
      permissionsPapel: PermissionsPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermissionsPapelUpdateComponent,
    resolve: {
      permissionsPapel: PermissionsPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermissionsPapelUpdateComponent,
    resolve: {
      permissionsPapel: PermissionsPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permissionsPapelRoute)],
  exports: [RouterModule],
})
export class PermissionsPapelRoutingModule {}
