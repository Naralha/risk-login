import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GrupoDetailComponent } from './grupo-detail.component';

describe('Grupo Management Detail Component', () => {
  let comp: GrupoDetailComponent;
  let fixture: ComponentFixture<GrupoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GrupoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ grupo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GrupoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GrupoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load grupo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.grupo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
