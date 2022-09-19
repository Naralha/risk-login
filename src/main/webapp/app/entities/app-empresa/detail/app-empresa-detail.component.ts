import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppEmpresa } from '../app-empresa.model';

@Component({
  selector: 'jhi-app-empresa-detail',
  templateUrl: './app-empresa-detail.component.html',
})
export class AppEmpresaDetailComponent implements OnInit {
  appEmpresa: IAppEmpresa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appEmpresa }) => {
      this.appEmpresa = appEmpresa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
