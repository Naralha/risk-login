import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UsuarioPapelComponent } from './list/usuario-papel.component';
import { UsuarioPapelDetailComponent } from './detail/usuario-papel-detail.component';
import { UsuarioPapelUpdateComponent } from './update/usuario-papel-update.component';
import { UsuarioPapelDeleteDialogComponent } from './delete/usuario-papel-delete-dialog.component';
import { UsuarioPapelRoutingModule } from './route/usuario-papel-routing.module';

@NgModule({
  imports: [SharedModule, UsuarioPapelRoutingModule],
  declarations: [UsuarioPapelComponent, UsuarioPapelDetailComponent, UsuarioPapelUpdateComponent, UsuarioPapelDeleteDialogComponent],
  entryComponents: [UsuarioPapelDeleteDialogComponent],
})
export class UsuarioPapelModule {}
