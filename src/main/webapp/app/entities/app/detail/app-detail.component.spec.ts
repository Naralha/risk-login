import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppDetailComponent } from './app-detail.component';

describe('App Management Detail Component', () => {
  let comp: AppDetailComponent;
  let fixture: ComponentFixture<AppDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ app: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AppDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AppDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load app on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.app).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
