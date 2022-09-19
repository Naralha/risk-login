import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioGrupo } from '../usuario-grupo.model';
import { UsuarioGrupoService } from '../service/usuario-grupo.service';
import { UsuarioGrupoDeleteDialogComponent } from '../delete/usuario-grupo-delete-dialog.component';

@Component({
  selector: 'jhi-usuario-grupo',
  templateUrl: './usuario-grupo.component.html',
})
export class UsuarioGrupoComponent implements OnInit {
  usuarioGrupos?: IUsuarioGrupo[];
  isLoading = false;

  constructor(protected usuarioGrupoService: UsuarioGrupoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.usuarioGrupoService.query().subscribe({
      next: (res: HttpResponse<IUsuarioGrupo[]>) => {
        this.isLoading = false;
        this.usuarioGrupos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IUsuarioGrupo): number {
    return item.id!;
  }

  delete(usuarioGrupo: IUsuarioGrupo): void {
    const modalRef = this.modalService.open(UsuarioGrupoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.usuarioGrupo = usuarioGrupo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
