import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioPapel } from '../usuario-papel.model';
import { UsuarioPapelService } from '../service/usuario-papel.service';
import { UsuarioPapelDeleteDialogComponent } from '../delete/usuario-papel-delete-dialog.component';

@Component({
  selector: 'jhi-usuario-papel',
  templateUrl: './usuario-papel.component.html',
})
export class UsuarioPapelComponent implements OnInit {
  usuarioPapels?: IUsuarioPapel[];
  isLoading = false;

  constructor(protected usuarioPapelService: UsuarioPapelService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.usuarioPapelService.query().subscribe({
      next: (res: HttpResponse<IUsuarioPapel[]>) => {
        this.isLoading = false;
        this.usuarioPapels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IUsuarioPapel): number {
    return item.id!;
  }

  delete(usuarioPapel: IUsuarioPapel): void {
    const modalRef = this.modalService.open(UsuarioPapelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.usuarioPapel = usuarioPapel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
