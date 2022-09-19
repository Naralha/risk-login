import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UsuarioGrupoComponent } from '../list/usuario-grupo.component';
import { UsuarioGrupoDetailComponent } from '../detail/usuario-grupo-detail.component';
import { UsuarioGrupoUpdateComponent } from '../update/usuario-grupo-update.component';
import { UsuarioGrupoRoutingResolveService } from './usuario-grupo-routing-resolve.service';

const usuarioGrupoRoute: Routes = [
  {
    path: '',
    component: UsuarioGrupoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UsuarioGrupoDetailComponent,
    resolve: {
      usuarioGrupo: UsuarioGrupoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UsuarioGrupoUpdateComponent,
    resolve: {
      usuarioGrupo: UsuarioGrupoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UsuarioGrupoUpdateComponent,
    resolve: {
      usuarioGrupo: UsuarioGrupoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(usuarioGrupoRoute)],
  exports: [RouterModule],
})
export class UsuarioGrupoRoutingModule {}
