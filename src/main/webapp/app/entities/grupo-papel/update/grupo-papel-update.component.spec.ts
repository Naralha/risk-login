import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GrupoPapelService } from '../service/grupo-papel.service';
import { IGrupoPapel, GrupoPapel } from '../grupo-papel.model';
import { IGrupo } from 'app/entities/grupo/grupo.model';
import { GrupoService } from 'app/entities/grupo/service/grupo.service';
import { IPapel } from 'app/entities/papel/papel.model';
import { PapelService } from 'app/entities/papel/service/papel.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { GrupoPapelUpdateComponent } from './grupo-papel-update.component';

describe('GrupoPapel Management Update Component', () => {
  let comp: GrupoPapelUpdateComponent;
  let fixture: ComponentFixture<GrupoPapelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let grupoPapelService: GrupoPapelService;
  let grupoService: GrupoService;
  let papelService: PapelService;
  let empresaService: EmpresaService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GrupoPapelUpdateComponent],
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
      .overrideTemplate(GrupoPapelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GrupoPapelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    grupoPapelService = TestBed.inject(GrupoPapelService);
    grupoService = TestBed.inject(GrupoService);
    papelService = TestBed.inject(PapelService);
    empresaService = TestBed.inject(EmpresaService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Grupo query and add missing value', () => {
      const grupoPapel: IGrupoPapel = { id: 456 };
      const grupo: IGrupo = { id: 14463 };
      grupoPapel.grupo = grupo;

      const grupoCollection: IGrupo[] = [{ id: 54060 }];
      jest.spyOn(grupoService, 'query').mockReturnValue(of(new HttpResponse({ body: grupoCollection })));
      const additionalGrupos = [grupo];
      const expectedCollection: IGrupo[] = [...additionalGrupos, ...grupoCollection];
      jest.spyOn(grupoService, 'addGrupoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      expect(grupoService.query).toHaveBeenCalled();
      expect(grupoService.addGrupoToCollectionIfMissing).toHaveBeenCalledWith(grupoCollection, ...additionalGrupos);
      expect(comp.gruposSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Papel query and add missing value', () => {
      const grupoPapel: IGrupoPapel = { id: 456 };
      const papel: IPapel = { id: 29393 };
      grupoPapel.papel = papel;

      const papelCollection: IPapel[] = [{ id: 53693 }];
      jest.spyOn(papelService, 'query').mockReturnValue(of(new HttpResponse({ body: papelCollection })));
      const additionalPapels = [papel];
      const expectedCollection: IPapel[] = [...additionalPapels, ...papelCollection];
      jest.spyOn(papelService, 'addPapelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      expect(papelService.query).toHaveBeenCalled();
      expect(papelService.addPapelToCollectionIfMissing).toHaveBeenCalledWith(papelCollection, ...additionalPapels);
      expect(comp.papelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empresa query and add missing value', () => {
      const grupoPapel: IGrupoPapel = { id: 456 };
      const empresa: IEmpresa = { id: 97726 };
      grupoPapel.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 63012 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const grupoPapel: IGrupoPapel = { id: 456 };
      const usuario: IUsuario = { id: 71575 };
      grupoPapel.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 97351 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const grupoPapel: IGrupoPapel = { id: 456 };
      const grupo: IGrupo = { id: 1290 };
      grupoPapel.grupo = grupo;
      const papel: IPapel = { id: 82966 };
      grupoPapel.papel = papel;
      const empresa: IEmpresa = { id: 39166 };
      grupoPapel.empresa = empresa;
      const usuario: IUsuario = { id: 2071 };
      grupoPapel.usuario = usuario;

      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(grupoPapel));
      expect(comp.gruposSharedCollection).toContain(grupo);
      expect(comp.papelsSharedCollection).toContain(papel);
      expect(comp.empresasSharedCollection).toContain(empresa);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GrupoPapel>>();
      const grupoPapel = { id: 123 };
      jest.spyOn(grupoPapelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: grupoPapel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(grupoPapelService.update).toHaveBeenCalledWith(grupoPapel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GrupoPapel>>();
      const grupoPapel = new GrupoPapel();
      jest.spyOn(grupoPapelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: grupoPapel }));
      saveSubject.complete();

      // THEN
      expect(grupoPapelService.create).toHaveBeenCalledWith(grupoPapel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GrupoPapel>>();
      const grupoPapel = { id: 123 };
      jest.spyOn(grupoPapelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupoPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(grupoPapelService.update).toHaveBeenCalledWith(grupoPapel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGrupoById', () => {
      it('Should return tracked Grupo primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGrupoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPapelById', () => {
      it('Should return tracked Papel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPapelById(0, entity);
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
