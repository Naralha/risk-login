import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UsuarioPapelComponent } from '../list/usuario-papel.component';
import { UsuarioPapelDetailComponent } from '../detail/usuario-papel-detail.component';
import { UsuarioPapelUpdateComponent } from '../update/usuario-papel-update.component';
import { UsuarioPapelRoutingResolveService } from './usuario-papel-routing-resolve.service';

const usuarioPapelRoute: Routes = [
  {
    path: '',
    component: UsuarioPapelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UsuarioPapelDetailComponent,
    resolve: {
      usuarioPapel: UsuarioPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UsuarioPapelUpdateComponent,
    resolve: {
      usuarioPapel: UsuarioPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UsuarioPapelUpdateComponent,
    resolve: {
      usuarioPapel: UsuarioPapelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(usuarioPapelRoute)],
  exports: [RouterModule],
})
export class UsuarioPapelRoutingModule {}
