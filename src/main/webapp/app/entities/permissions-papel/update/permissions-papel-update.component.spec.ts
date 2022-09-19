import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PermissionsPapelService } from '../service/permissions-papel.service';
import { IPermissionsPapel, PermissionsPapel } from '../permissions-papel.model';
import { IPapel } from 'app/entities/papel/papel.model';
import { PapelService } from 'app/entities/papel/service/papel.service';
import { IPermissions } from 'app/entities/permissions/permissions.model';
import { PermissionsService } from 'app/entities/permissions/service/permissions.service';
import { IFeatures } from 'app/entities/features/features.model';
import { FeaturesService } from 'app/entities/features/service/features.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { PermissionsPapelUpdateComponent } from './permissions-papel-update.component';

describe('PermissionsPapel Management Update Component', () => {
  let comp: PermissionsPapelUpdateComponent;
  let fixture: ComponentFixture<PermissionsPapelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permissionsPapelService: PermissionsPapelService;
  let papelService: PapelService;
  let permissionsService: PermissionsService;
  let featuresService: FeaturesService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PermissionsPapelUpdateComponent],
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
      .overrideTemplate(PermissionsPapelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionsPapelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permissionsPapelService = TestBed.inject(PermissionsPapelService);
    papelService = TestBed.inject(PapelService);
    permissionsService = TestBed.inject(PermissionsService);
    featuresService = TestBed.inject(FeaturesService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Papel query and add missing value', () => {
      const permissionsPapel: IPermissionsPapel = { id: 456 };
      const papel: IPapel = { id: 30613 };
      permissionsPapel.papel = papel;

      const papelCollection: IPapel[] = [{ id: 18520 }];
      jest.spyOn(papelService, 'query').mockReturnValue(of(new HttpResponse({ body: papelCollection })));
      const additionalPapels = [papel];
      const expectedCollection: IPapel[] = [...additionalPapels, ...papelCollection];
      jest.spyOn(papelService, 'addPapelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      expect(papelService.query).toHaveBeenCalled();
      expect(papelService.addPapelToCollectionIfMissing).toHaveBeenCalledWith(papelCollection, ...additionalPapels);
      expect(comp.papelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Permissions query and add missing value', () => {
      const permissionsPapel: IPermissionsPapel = { id: 456 };
      const permissions: IPermissions = { id: 21556 };
      permissionsPapel.permissions = permissions;

      const permissionsCollection: IPermissions[] = [{ id: 44041 }];
      jest.spyOn(permissionsService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionsCollection })));
      const additionalPermissions = [permissions];
      const expectedCollection: IPermissions[] = [...additionalPermissions, ...permissionsCollection];
      jest.spyOn(permissionsService, 'addPermissionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      expect(permissionsService.query).toHaveBeenCalled();
      expect(permissionsService.addPermissionsToCollectionIfMissing).toHaveBeenCalledWith(permissionsCollection, ...additionalPermissions);
      expect(comp.permissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Features query and add missing value', () => {
      const permissionsPapel: IPermissionsPapel = { id: 456 };
      const features: IFeatures = { id: 55052 };
      permissionsPapel.features = features;

      const featuresCollection: IFeatures[] = [{ id: 39002 }];
      jest.spyOn(featuresService, 'query').mockReturnValue(of(new HttpResponse({ body: featuresCollection })));
      const additionalFeatures = [features];
      const expectedCollection: IFeatures[] = [...additionalFeatures, ...featuresCollection];
      jest.spyOn(featuresService, 'addFeaturesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      expect(featuresService.query).toHaveBeenCalled();
      expect(featuresService.addFeaturesToCollectionIfMissing).toHaveBeenCalledWith(featuresCollection, ...additionalFeatures);
      expect(comp.featuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const permissionsPapel: IPermissionsPapel = { id: 456 };
      const usuario: IUsuario = { id: 52796 };
      permissionsPapel.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 40739 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const permissionsPapel: IPermissionsPapel = { id: 456 };
      const papel: IPapel = { id: 55623 };
      permissionsPapel.papel = papel;
      const permissions: IPermissions = { id: 91874 };
      permissionsPapel.permissions = permissions;
      const features: IFeatures = { id: 19664 };
      permissionsPapel.features = features;
      const usuario: IUsuario = { id: 53979 };
      permissionsPapel.usuario = usuario;

      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(permissionsPapel));
      expect(comp.papelsSharedCollection).toContain(papel);
      expect(comp.permissionsSharedCollection).toContain(permissions);
      expect(comp.featuresSharedCollection).toContain(features);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PermissionsPapel>>();
      const permissionsPapel = { id: 123 };
      jest.spyOn(permissionsPapelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionsPapel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(permissionsPapelService.update).toHaveBeenCalledWith(permissionsPapel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PermissionsPapel>>();
      const permissionsPapel = new PermissionsPapel();
      jest.spyOn(permissionsPapelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionsPapel }));
      saveSubject.complete();

      // THEN
      expect(permissionsPapelService.create).toHaveBeenCalledWith(permissionsPapel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PermissionsPapel>>();
      const permissionsPapel = { id: 123 };
      jest.spyOn(permissionsPapelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionsPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permissionsPapelService.update).toHaveBeenCalledWith(permissionsPapel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPapelById', () => {
      it('Should return tracked Papel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPapelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPermissionsById', () => {
      it('Should return tracked Permissions primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPermissionsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFeaturesById', () => {
      it('Should return tracked Features primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFeaturesById(0, entity);
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
