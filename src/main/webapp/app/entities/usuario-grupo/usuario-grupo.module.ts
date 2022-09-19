import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UsuarioGrupoComponent } from './list/usuario-grupo.component';
import { UsuarioGrupoDetailComponent } from './detail/usuario-grupo-detail.component';
import { UsuarioGrupoUpdateComponent } from './update/usuario-grupo-update.component';
import { UsuarioGrupoDeleteDialogComponent } from './delete/usuario-grupo-delete-dialog.component';
import { UsuarioGrupoRoutingModule } from './route/usuario-grupo-routing.module';

@NgModule({
  imports: [SharedModule, UsuarioGrupoRoutingModule],
  declarations: [UsuarioGrupoComponent, UsuarioGrupoDetailComponent, UsuarioGrupoUpdateComponent, UsuarioGrupoDeleteDialogComponent],
  entryComponents: [UsuarioGrupoDeleteDialogComponent],
})
export class UsuarioGrupoModule {}
