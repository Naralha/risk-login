import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGrupo } from '../grupo.model';
import { GrupoService } from '../service/grupo.service';

@Component({
  templateUrl: './grupo-delete-dialog.component.html',
})
export class GrupoDeleteDialogComponent {
  grupo?: IGrupo;

  constructor(protected grupoService: GrupoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.grupoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
