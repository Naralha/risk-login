import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PapelService } from '../service/papel.service';
import { IPapel, Papel } from '../papel.model';
import { IApp } from 'app/entities/app/app.model';
import { AppService } from 'app/entities/app/service/app.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { PapelUpdateComponent } from './papel-update.component';

describe('Papel Management Update Component', () => {
  let comp: PapelUpdateComponent;
  let fixture: ComponentFixture<PapelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let papelService: PapelService;
  let appService: AppService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PapelUpdateComponent],
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
      .overrideTemplate(PapelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PapelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    papelService = TestBed.inject(PapelService);
    appService = TestBed.inject(AppService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call App query and add missing value', () => {
      const papel: IPapel = { id: 456 };
      const app: IApp = { id: 34814 };
      papel.app = app;

      const appCollection: IApp[] = [{ id: 3098 }];
      jest.spyOn(appService, 'query').mockReturnValue(of(new HttpResponse({ body: appCollection })));
      const additionalApps = [app];
      const expectedCollection: IApp[] = [...additionalApps, ...appCollection];
      jest.spyOn(appService, 'addAppToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ papel });
      comp.ngOnInit();

      expect(appService.query).toHaveBeenCalled();
      expect(appService.addAppToCollectionIfMissing).toHaveBeenCalledWith(appCollection, ...additionalApps);
      expect(comp.appsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const papel: IPapel = { id: 456 };
      const usuario: IUsuario = { id: 71952 };
      papel.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 28386 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ papel });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const papel: IPapel = { id: 456 };
      const app: IApp = { id: 47719 };
      papel.app = app;
      const usuario: IUsuario = { id: 59765 };
      papel.usuario = usuario;

      activatedRoute.data = of({ papel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(papel));
      expect(comp.appsSharedCollection).toContain(app);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Papel>>();
      const papel = { id: 123 };
      jest.spyOn(papelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ papel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: papel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(papelService.update).toHaveBeenCalledWith(papel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Papel>>();
      const papel = new Papel();
      jest.spyOn(papelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ papel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: papel }));
      saveSubject.complete();

      // THEN
      expect(papelService.create).toHaveBeenCalledWith(papel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Papel>>();
      const papel = { id: 123 };
      jest.spyOn(papelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ papel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(papelService.update).toHaveBeenCalledWith(papel);
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
