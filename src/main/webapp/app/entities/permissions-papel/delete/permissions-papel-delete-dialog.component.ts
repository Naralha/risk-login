import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionsPapel } from '../permissions-papel.model';
import { PermissionsPapelService } from '../service/permissions-papel.service';

@Component({
  templateUrl: './permissions-papel-delete-dialog.component.html',
})
export class PermissionsPapelDeleteDialogComponent {
  permissionsPapel?: IPermissionsPapel;

  constructor(protected permissionsPapelService: PermissionsPapelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionsPapelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
