import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppEmpresa } from '../app-empresa.model';
import { AppEmpresaService } from '../service/app-empresa.service';

@Component({
  templateUrl: './app-empresa-delete-dialog.component.html',
})
export class AppEmpresaDeleteDialogComponent {
  appEmpresa?: IAppEmpresa;

  constructor(protected appEmpresaService: AppEmpresaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appEmpresaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
