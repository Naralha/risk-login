import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermissionsPapel } from '../permissions-papel.model';

@Component({
  selector: 'jhi-permissions-papel-detail',
  templateUrl: './permissions-papel-detail.component.html',
})
export class PermissionsPapelDetailComponent implements OnInit {
  permissionsPapel: IPermissionsPapel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionsPapel }) => {
      this.permissionsPapel = permissionsPapel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
