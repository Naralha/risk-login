import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPapel } from '../papel.model';
import { PapelService } from '../service/papel.service';
import { PapelDeleteDialogComponent } from '../delete/papel-delete-dialog.component';

@Component({
  selector: 'jhi-papel',
  templateUrl: './papel.component.html',
})
export class PapelComponent implements OnInit {
  papels?: IPapel[];
  isLoading = false;

  constructor(protected papelService: PapelService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.papelService.query().subscribe({
      next: (res: HttpResponse<IPapel[]>) => {
        this.isLoading = false;
        this.papels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPapel): number {
    return item.id!;
  }

  delete(papel: IPapel): void {
    const modalRef = this.modalService.open(PapelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.papel = papel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
