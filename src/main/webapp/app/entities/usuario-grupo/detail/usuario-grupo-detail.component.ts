import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUsuarioGrupo } from '../usuario-grupo.model';

@Component({
  selector: 'jhi-usuario-grupo-detail',
  templateUrl: './usuario-grupo-detail.component.html',
})
export class UsuarioGrupoDetailComponent implements OnInit {
  usuarioGrupo: IUsuarioGrupo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioGrupo }) => {
      this.usuarioGrupo = usuarioGrupo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
