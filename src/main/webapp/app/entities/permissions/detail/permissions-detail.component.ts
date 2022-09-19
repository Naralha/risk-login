import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermissions } from '../permissions.model';

@Component({
  selector: 'jhi-permissions-detail',
  templateUrl: './permissions-detail.component.html',
})
export class PermissionsDetailComponent implements OnInit {
  permissions: IPermissions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissions }) => {
      this.permissions = permissions;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
