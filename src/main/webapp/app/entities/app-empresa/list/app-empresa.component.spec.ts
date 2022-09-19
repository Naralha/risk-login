import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AppEmpresaService } from '../service/app-empresa.service';

import { AppEmpresaComponent } from './app-empresa.component';

describe('AppEmpresa Management Component', () => {
  let comp: AppEmpresaComponent;
  let fixture: ComponentFixture<AppEmpresaComponent>;
  let service: AppEmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AppEmpresaComponent],
    })
      .overrideTemplate(AppEmpresaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppEmpresaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AppEmpresaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.appEmpresas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
