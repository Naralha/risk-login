import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGrupo } from '../grupo.model';

@Component({
  selector: 'jhi-grupo-detail',
  templateUrl: './grupo-detail.component.html',
})
export class GrupoDetailComponent implements OnInit {
  grupo: IGrupo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grupo }) => {
      this.grupo = grupo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
