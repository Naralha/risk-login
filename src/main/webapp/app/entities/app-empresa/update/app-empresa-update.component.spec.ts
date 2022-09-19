import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AppEmpresaService } from '../service/app-empresa.service';
import { IAppEmpresa, AppEmpresa } from '../app-empresa.model';
import { IApp } from 'app/entities/app/app.model';
import { AppService } from 'app/entities/app/service/app.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { AppEmpresaUpdateComponent } from './app-empresa-update.component';

describe('AppEmpresa Management Update Component', () => {
  let comp: AppEmpresaUpdateComponent;
  let fixture: ComponentFixture<AppEmpresaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appEmpresaService: AppEmpresaService;
  let appService: AppService;
  let empresaService: EmpresaService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AppEmpresaUpdateComponent],
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
      .overrideTemplate(AppEmpresaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppEmpresaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appEmpresaService = TestBed.inject(AppEmpresaService);
    appService = TestBed.inject(AppService);
    empresaService = TestBed.inject(EmpresaService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call App query and add missing value', () => {
      const appEmpresa: IAppEmpresa = { id: 456 };
      const app: IApp = { id: 74390 };
      appEmpresa.app = app;

      const appCollection: IApp[] = [{ id: 83977 }];
      jest.spyOn(appService, 'query').mockReturnValue(of(new HttpResponse({ body: appCollection })));
      const additionalApps = [app];
      const expectedCollection: IApp[] = [...additionalApps, ...appCollection];
      jest.spyOn(appService, 'addAppToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      expect(appService.query).toHaveBeenCalled();
      expect(appService.addAppToCollectionIfMissing).toHaveBeenCalledWith(appCollection, ...additionalApps);
      expect(comp.appsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empresa query and add missing value', () => {
      const appEmpresa: IAppEmpresa = { id: 456 };
      const empresa: IEmpresa = { id: 55979 };
      appEmpresa.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 42901 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const appEmpresa: IAppEmpresa = { id: 456 };
      const usuario: IUsuario = { id: 38653 };
      appEmpresa.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 65928 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appEmpresa: IAppEmpresa = { id: 456 };
      const app: IApp = { id: 69108 };
      appEmpresa.app = app;
      const empresa: IEmpresa = { id: 6547 };
      appEmpresa.empresa = empresa;
      const usuario: IUsuario = { id: 33887 };
      appEmpresa.usuario = usuario;

      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(appEmpresa));
      expect(comp.appsSharedCollection).toContain(app);
      expect(comp.empresasSharedCollection).toContain(empresa);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppEmpresa>>();
      const appEmpresa = { id: 123 };
      jest.spyOn(appEmpresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appEmpresa }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(appEmpresaService.update).toHaveBeenCalledWith(appEmpresa);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppEmpresa>>();
      const appEmpresa = new AppEmpresa();
      jest.spyOn(appEmpresaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appEmpresa }));
      saveSubject.complete();

      // THEN
      expect(appEmpresaService.create).toHaveBeenCalledWith(appEmpresa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppEmpresa>>();
      const appEmpresa = { id: 123 };
      jest.spyOn(appEmpresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appEmpresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appEmpresaService.update).toHaveBeenCalledWith(appEmpresa);
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

    describe('trackEmpresaById', () => {
      it('Should return tracked Empresa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpresaById(0, entity);
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
