import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioPapel } from '../usuario-papel.model';
import { UsuarioPapelService } from '../service/usuario-papel.service';

@Component({
  templateUrl: './usuario-papel-delete-dialog.component.html',
})
export class UsuarioPapelDeleteDialogComponent {
  usuarioPapel?: IUsuarioPapel;

  constructor(protected usuarioPapelService: UsuarioPapelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.usuarioPapelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
