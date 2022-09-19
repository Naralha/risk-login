import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUsuarioPapel } from '../usuario-papel.model';

@Component({
  selector: 'jhi-usuario-papel-detail',
  templateUrl: './usuario-papel-detail.component.html',
})
export class UsuarioPapelDetailComponent implements OnInit {
  usuarioPapel: IUsuarioPapel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioPapel }) => {
      this.usuarioPapel = usuarioPapel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
