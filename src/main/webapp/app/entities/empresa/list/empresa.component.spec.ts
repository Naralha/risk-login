import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EmpresaService } from '../service/empresa.service';

import { EmpresaComponent } from './empresa.component';

describe('Empresa Management Component', () => {
  let comp: EmpresaComponent;
  let fixture: ComponentFixture<EmpresaComponent>;
  let service: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmpresaComponent],
    })
      .overrideTemplate(EmpresaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpresaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmpresaService);

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
    expect(comp.empresas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
