import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppEmpresaDetailComponent } from './app-empresa-detail.component';

describe('AppEmpresa Management Detail Component', () => {
  let comp: AppEmpresaDetailComponent;
  let fixture: ComponentFixture<AppEmpresaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppEmpresaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ appEmpresa: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AppEmpresaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AppEmpresaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load appEmpresa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.appEmpresa).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
