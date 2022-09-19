import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppComponent } from '../list/app.component';
import { AppDetailComponent } from '../detail/app-detail.component';
import { AppUpdateComponent } from '../update/app-update.component';
import { AppRoutingResolveService } from './app-routing-resolve.service';

const appRoute: Routes = [
  {
    path: '',
    component: AppComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppDetailComponent,
    resolve: {
      app: AppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppUpdateComponent,
    resolve: {
      app: AppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppUpdateComponent,
    resolve: {
      app: AppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appRoute)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
