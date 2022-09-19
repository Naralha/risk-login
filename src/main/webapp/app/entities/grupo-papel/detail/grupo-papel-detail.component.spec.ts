import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GrupoPapelDetailComponent } from './grupo-papel-detail.component';

describe('GrupoPapel Management Detail Component', () => {
  let comp: GrupoPapelDetailComponent;
  let fixture: ComponentFixture<GrupoPapelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GrupoPapelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ grupoPapel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GrupoPapelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GrupoPapelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load grupoPapel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.grupoPapel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
