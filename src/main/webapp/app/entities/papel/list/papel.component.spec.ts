import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PapelService } from '../service/papel.service';

import { PapelComponent } from './papel.component';

describe('Papel Management Component', () => {
  let comp: PapelComponent;
  let fixture: ComponentFixture<PapelComponent>;
  let service: PapelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PapelComponent],
    })
      .overrideTemplate(PapelComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PapelComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PapelService);

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
    expect(comp.papels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
