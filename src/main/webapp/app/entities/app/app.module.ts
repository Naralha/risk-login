import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppComponent } from './list/app.component';
import { AppDetailComponent } from './detail/app-detail.component';
import { AppUpdateComponent } from './update/app-update.component';
import { AppDeleteDialogComponent } from './delete/app-delete-dialog.component';
import { AppRoutingModule } from './route/app-routing.module';

@NgModule({
  imports: [SharedModule, AppRoutingModule],
  declarations: [AppComponent, AppDetailComponent, AppUpdateComponent, AppDeleteDialogComponent],
  entryComponents: [AppDeleteDialogComponent],
})
export class AppModule {}
