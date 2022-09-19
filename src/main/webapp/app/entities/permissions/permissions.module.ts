import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermissionsComponent } from './list/permissions.component';
import { PermissionsDetailComponent } from './detail/permissions-detail.component';
import { PermissionsUpdateComponent } from './update/permissions-update.component';
import { PermissionsDeleteDialogComponent } from './delete/permissions-delete-dialog.component';
import { PermissionsRoutingModule } from './route/permissions-routing.module';

@NgModule({
  imports: [SharedModule, PermissionsRoutingModule],
  declarations: [PermissionsComponent, PermissionsDetailComponent, PermissionsUpdateComponent, PermissionsDeleteDialogComponent],
  entryComponents: [PermissionsDeleteDialogComponent],
})
export class PermissionsModule {}
