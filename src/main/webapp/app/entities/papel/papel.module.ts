import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PapelComponent } from './list/papel.component';
import { PapelDetailComponent } from './detail/papel-detail.component';
import { PapelUpdateComponent } from './update/papel-update.component';
import { PapelDeleteDialogComponent } from './delete/papel-delete-dialog.component';
import { PapelRoutingModule } from './route/papel-routing.module';

@NgModule({
  imports: [SharedModule, PapelRoutingModule],
  declarations: [PapelComponent, PapelDetailComponent, PapelUpdateComponent, PapelDeleteDialogComponent],
  entryComponents: [PapelDeleteDialogComponent],
})
export class PapelModule {}
