import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GrupoPapelService } from '../service/grupo-papel.service';

import { GrupoPapelComponent } from './grupo-papel.component';

describe('GrupoPapel Management Component', () => {
  let comp: GrupoPapelComponent;
  let fixture: ComponentFixture<GrupoPapelComponent>;
  let service: GrupoPapelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GrupoPapelComponent],
    })
      .overrideTemplate(GrupoPapelComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GrupoPapelComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GrupoPapelService);

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
    expect(comp.grupoPapels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
