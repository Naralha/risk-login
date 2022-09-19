import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermissionsComponent } from '../list/permissions.component';
import { PermissionsDetailComponent } from '../detail/permissions-detail.component';
import { PermissionsUpdateComponent } from '../update/permissions-update.component';
import { PermissionsRoutingResolveService } from './permissions-routing-resolve.service';

const permissionsRoute: Routes = [
  {
    path: '',
    component: PermissionsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermissionsDetailComponent,
    resolve: {
      permissions: PermissionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermissionsUpdateComponent,
    resolve: {
      permissions: PermissionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermissionsUpdateComponent,
    resolve: {
      permissions: PermissionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permissionsRoute)],
  exports: [RouterModule],
})
export class PermissionsRoutingModule {}
