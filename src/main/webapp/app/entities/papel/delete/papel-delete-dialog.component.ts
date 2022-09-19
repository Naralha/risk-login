import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPapel } from '../papel.model';
import { PapelService } from '../service/papel.service';

@Component({
  templateUrl: './papel-delete-dialog.component.html',
})
export class PapelDeleteDialogComponent {
  papel?: IPapel;

  constructor(protected papelService: PapelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.papelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
