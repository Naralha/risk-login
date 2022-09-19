import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPapel } from '../papel.model';

@Component({
  selector: 'jhi-papel-detail',
  templateUrl: './papel-detail.component.html',
})
export class PapelDetailComponent implements OnInit {
  papel: IPapel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ papel }) => {
      this.papel = papel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
