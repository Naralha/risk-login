import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GrupoComponent } from './list/grupo.component';
import { GrupoDetailComponent } from './detail/grupo-detail.component';
import { GrupoUpdateComponent } from './update/grupo-update.component';
import { GrupoDeleteDialogComponent } from './delete/grupo-delete-dialog.component';
import { GrupoRoutingModule } from './route/grupo-routing.module';

@NgModule({
  imports: [SharedModule, GrupoRoutingModule],
  declarations: [GrupoComponent, GrupoDetailComponent, GrupoUpdateComponent, GrupoDeleteDialogComponent],
  entryComponents: [GrupoDeleteDialogComponent],
})
export class GrupoModule {}
