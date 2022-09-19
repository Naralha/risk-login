import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionsDetailComponent } from './permissions-detail.component';

describe('Permissions Management Detail Component', () => {
  let comp: PermissionsDetailComponent;
  let fixture: ComponentFixture<PermissionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ permissions: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PermissionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PermissionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load permissions on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.permissions).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
