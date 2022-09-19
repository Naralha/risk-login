import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FeaturesService } from '../service/features.service';
import { IFeatures, Features } from '../features.model';
import { IApp } from 'app/entities/app/app.model';
import { AppService } from 'app/entities/app/service/app.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { FeaturesUpdateComponent } from './features-update.component';

describe('Features Management Update Component', () => {
  let comp: FeaturesUpdateComponent;
  let fixture: ComponentFixture<FeaturesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let featuresService: FeaturesService;
  let appService: AppService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FeaturesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FeaturesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeaturesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    featuresService = TestBed.inject(FeaturesService);
    appService = TestBed.inject(AppService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call App query and add missing value', () => {
      const features: IFeatures = { id: 456 };
      const app: IApp = { id: 75920 };
      features.app = app;

      const appCollection: IApp[] = [{ id: 3232 }];
      jest.spyOn(appService, 'query').mockReturnValue(of(new HttpResponse({ body: appCollection })));
      const additionalApps = [app];
      const expectedCollection: IApp[] = [...additionalApps, ...appCollection];
      jest.spyOn(appService, 'addAppToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ features });
      comp.ngOnInit();

      expect(appService.query).toHaveBeenCalled();
      expect(appService.addAppToCollectionIfMissing).toHaveBeenCalledWith(appCollection, ...additionalApps);
      expect(comp.appsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const features: IFeatures = { id: 456 };
      const usuario: IUsuario = { id: 32267 };
      features.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 83777 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ features });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const features: IFeatures = { id: 456 };
      const app: IApp = { id: 81753 };
      features.app = app;
      const usuario: IUsuario = { id: 88276 };
      features.usuario = usuario;

      activatedRoute.data = of({ features });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(features));
      expect(comp.appsSharedCollection).toContain(app);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Features>>();
      const features = { id: 123 };
      jest.spyOn(featuresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ features });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: features }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(featuresService.update).toHaveBeenCalledWith(features);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Features>>();
      const features = new Features();
      jest.spyOn(featuresService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ features });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: features }));
      saveSubject.complete();

      // THEN
      expect(featuresService.create).toHaveBeenCalledWith(features);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Features>>();
      const features = { id: 123 };
      jest.spyOn(featuresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ features });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(featuresService.update).toHaveBeenCalledWith(features);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAppById', () => {
      it('Should return tracked App primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAppById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
