import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGrupoPapel } from '../grupo-papel.model';
import { GrupoPapelService } from '../service/grupo-papel.service';

@Component({
  templateUrl: './grupo-papel-delete-dialog.component.html',
})
export class GrupoPapelDeleteDialogComponent {
  grupoPapel?: IGrupoPapel;

  constructor(protected grupoPapelService: GrupoPapelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.grupoPapelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
