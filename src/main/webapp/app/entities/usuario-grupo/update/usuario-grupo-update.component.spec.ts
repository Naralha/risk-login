import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UsuarioGrupoService } from '../service/usuario-grupo.service';
import { IUsuarioGrupo, UsuarioGrupo } from '../usuario-grupo.model';
import { IGrupo } from 'app/entities/grupo/grupo.model';
import { GrupoService } from 'app/entities/grupo/service/grupo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { UsuarioGrupoUpdateComponent } from './usuario-grupo-update.component';

describe('UsuarioGrupo Management Update Component', () => {
  let comp: UsuarioGrupoUpdateComponent;
  let fixture: ComponentFixture<UsuarioGrupoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioGrupoService: UsuarioGrupoService;
  let grupoService: GrupoService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UsuarioGrupoUpdateComponent],
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
      .overrideTemplate(UsuarioGrupoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioGrupoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioGrupoService = TestBed.inject(UsuarioGrupoService);
    grupoService = TestBed.inject(GrupoService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Grupo query and add missing value', () => {
      const usuarioGrupo: IUsuarioGrupo = { id: 456 };
      const grupo: IGrupo = { id: 33308 };
      usuarioGrupo.grupo = grupo;

      const grupoCollection: IGrupo[] = [{ id: 82760 }];
      jest.spyOn(grupoService, 'query').mockReturnValue(of(new HttpResponse({ body: grupoCollection })));
      const additionalGrupos = [grupo];
      const expectedCollection: IGrupo[] = [...additionalGrupos, ...grupoCollection];
      jest.spyOn(grupoService, 'addGrupoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioGrupo });
      comp.ngOnInit();

      expect(grupoService.query).toHaveBeenCalled();
      expect(grupoService.addGrupoToCollectionIfMissing).toHaveBeenCalledWith(grupoCollection, ...additionalGrupos);
      expect(comp.gruposSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const usuarioGrupo: IUsuarioGrupo = { id: 456 };
      const usuario: IUsuario = { id: 2346 };
      usuarioGrupo.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 49939 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioGrupo });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuarioGrupo: IUsuarioGrupo = { id: 456 };
      const grupo: IGrupo = { id: 61117 };
      usuarioGrupo.grupo = grupo;
      const usuario: IUsuario = { id: 50446 };
      usuarioGrupo.usuario = usuario;

      activatedRoute.data = of({ usuarioGrupo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(usuarioGrupo));
      expect(comp.gruposSharedCollection).toContain(grupo);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UsuarioGrupo>>();
      const usuarioGrupo = { id: 123 };
      jest.spyOn(usuarioGrupoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioGrupo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioGrupo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioGrupoService.update).toHaveBeenCalledWith(usuarioGrupo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UsuarioGrupo>>();
      const usuarioGrupo = new UsuarioGrupo();
      jest.spyOn(usuarioGrupoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioGrupo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioGrupo }));
      saveSubject.complete();

      // THEN
      expect(usuarioGrupoService.create).toHaveBeenCalledWith(usuarioGrupo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UsuarioGrupo>>();
      const usuarioGrupo = { id: 123 };
      jest.spyOn(usuarioGrupoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioGrupo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioGrupoService.update).toHaveBeenCalledWith(usuarioGrupo);
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

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
