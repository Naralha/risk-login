import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioGrupo } from '../usuario-grupo.model';
import { UsuarioGrupoService } from '../service/usuario-grupo.service';

@Component({
  templateUrl: './usuario-grupo-delete-dialog.component.html',
})
export class UsuarioGrupoDeleteDialogComponent {
  usuarioGrupo?: IUsuarioGrupo;

  constructor(protected usuarioGrupoService: UsuarioGrupoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.usuarioGrupoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
