import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApp } from '../app.model';
import { AppService } from '../service/app.service';
import { AppDeleteDialogComponent } from '../delete/app-delete-dialog.component';

@Component({
  selector: 'jhi-app',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  apps?: IApp[];
  isLoading = false;

  constructor(protected appService: AppService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.appService.query().subscribe({
      next: (res: HttpResponse<IApp[]>) => {
        this.isLoading = false;
        this.apps = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApp): number {
    return item.id!;
  }

  delete(app: IApp): void {
    const modalRef = this.modalService.open(AppDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.app = app;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
