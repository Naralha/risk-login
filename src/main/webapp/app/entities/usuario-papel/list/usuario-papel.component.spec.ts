import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UsuarioPapelService } from '../service/usuario-papel.service';

import { UsuarioPapelComponent } from './usuario-papel.component';

describe('UsuarioPapel Management Component', () => {
  let comp: UsuarioPapelComponent;
  let fixture: ComponentFixture<UsuarioPapelComponent>;
  let service: UsuarioPapelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [UsuarioPapelComponent],
    })
      .overrideTemplate(UsuarioPapelComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioPapelComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UsuarioPapelService);

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
    expect(comp.usuarioPapels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
