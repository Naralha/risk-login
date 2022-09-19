import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppEmpresa } from '../app-empresa.model';
import { AppEmpresaService } from '../service/app-empresa.service';
import { AppEmpresaDeleteDialogComponent } from '../delete/app-empresa-delete-dialog.component';

@Component({
  selector: 'jhi-app-empresa',
  templateUrl: './app-empresa.component.html',
})
export class AppEmpresaComponent implements OnInit {
  appEmpresas?: IAppEmpresa[];
  isLoading = false;

  constructor(protected appEmpresaService: AppEmpresaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.appEmpresaService.query().subscribe({
      next: (res: HttpResponse<IAppEmpresa[]>) => {
        this.isLoading = false;
        this.appEmpresas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAppEmpresa): number {
    return item.id!;
  }

  delete(appEmpresa: IAppEmpresa): void {
    const modalRef = this.modalService.open(AppEmpresaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appEmpresa = appEmpresa;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
