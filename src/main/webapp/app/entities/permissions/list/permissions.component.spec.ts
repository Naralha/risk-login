import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PermissionsService } from '../service/permissions.service';

import { PermissionsComponent } from './permissions.component';

describe('Permissions Management Component', () => {
  let comp: PermissionsComponent;
  let fixture: ComponentFixture<PermissionsComponent>;
  let service: PermissionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PermissionsComponent],
    })
      .overrideTemplate(PermissionsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PermissionsService);

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
    expect(comp.permissions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
