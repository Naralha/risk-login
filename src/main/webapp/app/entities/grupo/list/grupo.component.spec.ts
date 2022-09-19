import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GrupoService } from '../service/grupo.service';

import { GrupoComponent } from './grupo.component';

describe('Grupo Management Component', () => {
  let comp: GrupoComponent;
  let fixture: ComponentFixture<GrupoComponent>;
  let service: GrupoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GrupoComponent],
    })
      .overrideTemplate(GrupoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GrupoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GrupoService);

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
    expect(comp.grupos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
