import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GrupoPapelComponent } from './list/grupo-papel.component';
import { GrupoPapelDetailComponent } from './detail/grupo-papel-detail.component';
import { GrupoPapelUpdateComponent } from './update/grupo-papel-update.component';
import { GrupoPapelDeleteDialogComponent } from './delete/grupo-papel-delete-dialog.component';
import { GrupoPapelRoutingModule } from './route/grupo-papel-routing.module';

@NgModule({
  imports: [SharedModule, GrupoPapelRoutingModule],
  declarations: [GrupoPapelComponent, GrupoPapelDetailComponent, GrupoPapelUpdateComponent, GrupoPapelDeleteDialogComponent],
  entryComponents: [GrupoPapelDeleteDialogComponent],
})
export class GrupoPapelModule {}
