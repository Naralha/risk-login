import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGrupo } from '../grupo.model';
import { GrupoService } from '../service/grupo.service';
import { GrupoDeleteDialogComponent } from '../delete/grupo-delete-dialog.component';

@Component({
  selector: 'jhi-grupo',
  templateUrl: './grupo.component.html',
})
export class GrupoComponent implements OnInit {
  grupos?: IGrupo[];
  isLoading = false;

  constructor(protected grupoService: GrupoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.grupoService.query().subscribe({
      next: (res: HttpResponse<IGrupo[]>) => {
        this.isLoading = false;
        this.grupos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IGrupo): number {
    return item.id!;
  }

  delete(grupo: IGrupo): void {
    const modalRef = this.modalService.open(GrupoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.grupo = grupo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
