import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UsuarioGrupoDetailComponent } from './usuario-grupo-detail.component';

describe('UsuarioGrupo Management Detail Component', () => {
  let comp: UsuarioGrupoDetailComponent;
  let fixture: ComponentFixture<UsuarioGrupoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UsuarioGrupoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ usuarioGrupo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UsuarioGrupoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UsuarioGrupoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load usuarioGrupo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.usuarioGrupo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
