import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppEmpresaComponent } from '../list/app-empresa.component';
import { AppEmpresaDetailComponent } from '../detail/app-empresa-detail.component';
import { AppEmpresaUpdateComponent } from '../update/app-empresa-update.component';
import { AppEmpresaRoutingResolveService } from './app-empresa-routing-resolve.service';

const appEmpresaRoute: Routes = [
  {
    path: '',
    component: AppEmpresaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppEmpresaDetailComponent,
    resolve: {
      appEmpresa: AppEmpresaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppEmpresaUpdateComponent,
    resolve: {
      appEmpresa: AppEmpresaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppEmpresaUpdateComponent,
    resolve: {
      appEmpresa: AppEmpresaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appEmpresaRoute)],
  exports: [RouterModule],
})
export class AppEmpresaRoutingModule {}
