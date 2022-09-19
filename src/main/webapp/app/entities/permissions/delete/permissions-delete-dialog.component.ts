import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissions } from '../permissions.model';
import { PermissionsService } from '../service/permissions.service';

@Component({
  templateUrl: './permissions-delete-dialog.component.html',
})
export class PermissionsDeleteDialogComponent {
  permissions?: IPermissions;

  constructor(protected permissionsService: PermissionsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
