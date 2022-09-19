import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissions } from '../permissions.model';
import { PermissionsService } from '../service/permissions.service';
import { PermissionsDeleteDialogComponent } from '../delete/permissions-delete-dialog.component';

@Component({
  selector: 'jhi-permissions',
  templateUrl: './permissions.component.html',
})
export class PermissionsComponent implements OnInit {
  permissions?: IPermissions[];
  isLoading = false;

  constructor(protected permissionsService: PermissionsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.permissionsService.query().subscribe({
      next: (res: HttpResponse<IPermissions[]>) => {
        this.isLoading = false;
        this.permissions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPermissions): number {
    return item.id!;
  }

  delete(permissions: IPermissions): void {
    const modalRef = this.modalService.open(PermissionsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.permissions = permissions;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
