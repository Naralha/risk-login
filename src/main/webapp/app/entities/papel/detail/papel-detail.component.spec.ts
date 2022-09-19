import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PapelDetailComponent } from './papel-detail.component';

describe('Papel Management Detail Component', () => {
  let comp: PapelDetailComponent;
  let fixture: ComponentFixture<PapelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PapelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ papel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PapelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PapelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load papel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.papel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
