import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApp } from '../app.model';
import { AppService } from '../service/app.service';

@Component({
  templateUrl: './app-delete-dialog.component.html',
})
export class AppDeleteDialogComponent {
  app?: IApp;

  constructor(protected appService: AppService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
