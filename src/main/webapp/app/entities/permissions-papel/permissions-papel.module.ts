import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermissionsPapelComponent } from './list/permissions-papel.component';
import { PermissionsPapelDetailComponent } from './detail/permissions-papel-detail.component';
import { PermissionsPapelUpdateComponent } from './update/permissions-papel-update.component';
import { PermissionsPapelDeleteDialogComponent } from './delete/permissions-papel-delete-dialog.component';
import { PermissionsPapelRoutingModule } from './route/permissions-papel-routing.module';

@NgModule({
  imports: [SharedModule, PermissionsPapelRoutingModule],
  declarations: [
    PermissionsPapelComponent,
    PermissionsPapelDetailComponent,
    PermissionsPapelUpdateComponent,
    PermissionsPapelDeleteDialogComponent,
  ],
  entryComponents: [PermissionsPapelDeleteDialogComponent],
})
export class PermissionsPapelModule {}
