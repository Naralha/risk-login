import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGrupoPapel } from '../grupo-papel.model';

@Component({
  selector: 'jhi-grupo-papel-detail',
  templateUrl: './grupo-papel-detail.component.html',
})
export class GrupoPapelDetailComponent implements OnInit {
  grupoPapel: IGrupoPapel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grupoPapel }) => {
      this.grupoPapel = grupoPapel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
