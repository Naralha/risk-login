import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UsuarioPapelDetailComponent } from './usuario-papel-detail.component';

describe('UsuarioPapel Management Detail Component', () => {
  let comp: UsuarioPapelDetailComponent;
  let fixture: ComponentFixture<UsuarioPapelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UsuarioPapelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ usuarioPapel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UsuarioPapelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UsuarioPapelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load usuarioPapel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.usuarioPapel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
