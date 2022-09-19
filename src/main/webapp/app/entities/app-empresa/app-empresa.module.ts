import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppEmpresaComponent } from './list/app-empresa.component';
import { AppEmpresaDetailComponent } from './detail/app-empresa-detail.component';
import { AppEmpresaUpdateComponent } from './update/app-empresa-update.component';
import { AppEmpresaDeleteDialogComponent } from './delete/app-empresa-delete-dialog.component';
import { AppEmpresaRoutingModule } from './route/app-empresa-routing.module';

@NgModule({
  imports: [SharedModule, AppEmpresaRoutingModule],
  declarations: [AppEmpresaComponent, AppEmpresaDetailComponent, AppEmpresaUpdateComponent, AppEmpresaDeleteDialogComponent],
  entryComponents: [AppEmpresaDeleteDialogComponent],
})
export class AppEmpresaModule {}
