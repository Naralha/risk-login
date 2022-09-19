import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionsPapelDetailComponent } from './permissions-papel-detail.component';

describe('PermissionsPapel Management Detail Component', () => {
  let comp: PermissionsPapelDetailComponent;
  let fixture: ComponentFixture<PermissionsPapelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionsPapelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ permissionsPapel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PermissionsPapelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PermissionsPapelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load permissionsPapel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.permissionsPapel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
