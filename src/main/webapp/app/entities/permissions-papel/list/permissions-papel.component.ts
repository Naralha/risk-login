import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionsPapel } from '../permissions-papel.model';
import { PermissionsPapelService } from '../service/permissions-papel.service';
import { PermissionsPapelDeleteDialogComponent } from '../delete/permissions-papel-delete-dialog.component';

@Component({
  selector: 'jhi-permissions-papel',
  templateUrl: './permissions-papel.component.html',
})
export class PermissionsPapelComponent implements OnInit {
  permissionsPapels?: IPermissionsPapel[];
  isLoading = false;

  constructor(protected permissionsPapelService: PermissionsPapelService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.permissionsPapelService.query().subscribe({
      next: (res: HttpResponse<IPermissionsPapel[]>) => {
        this.isLoading = false;
        this.permissionsPapels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPermissionsPapel): number {
    return item.id!;
  }

  delete(permissionsPapel: IPermissionsPapel): void {
    const modalRef = this.modalService.open(PermissionsPapelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.permissionsPapel = permissionsPapel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
