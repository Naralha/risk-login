import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PermissionsPapelService } from '../service/permissions-papel.service';

import { PermissionsPapelComponent } from './permissions-papel.component';

describe('PermissionsPapel Management Component', () => {
  let comp: PermissionsPapelComponent;
  let fixture: ComponentFixture<PermissionsPapelComponent>;
  let service: PermissionsPapelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PermissionsPapelComponent],
    })
      .overrideTemplate(PermissionsPapelComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionsPapelComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PermissionsPapelService);

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
    expect(comp.permissionsPapels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
