import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGrupoPapel } from '../grupo-papel.model';
import { GrupoPapelService } from '../service/grupo-papel.service';
import { GrupoPapelDeleteDialogComponent } from '../delete/grupo-papel-delete-dialog.component';

@Component({
  selector: 'jhi-grupo-papel',
  templateUrl: './grupo-papel.component.html',
})
export class GrupoPapelComponent implements OnInit {
  grupoPapels?: IGrupoPapel[];
  isLoading = false;

  constructor(protected grupoPapelService: GrupoPapelService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.grupoPapelService.query().subscribe({
      next: (res: HttpResponse<IGrupoPapel[]>) => {
        this.isLoading = false;
        this.grupoPapels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IGrupoPapel): number {
    return item.id!;
  }

  delete(grupoPapel: IGrupoPapel): void {
    const modalRef = this.modalService.open(GrupoPapelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.grupoPapel = grupoPapel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
