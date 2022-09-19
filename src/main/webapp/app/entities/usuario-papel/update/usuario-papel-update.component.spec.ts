import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UsuarioPapelService } from '../service/usuario-papel.service';
import { IUsuarioPapel, UsuarioPapel } from '../usuario-papel.model';
import { IPapel } from 'app/entities/papel/papel.model';
import { PapelService } from 'app/entities/papel/service/papel.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { UsuarioPapelUpdateComponent } from './usuario-papel-update.component';

describe('UsuarioPapel Management Update Component', () => {
  let comp: UsuarioPapelUpdateComponent;
  let fixture: ComponentFixture<UsuarioPapelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioPapelService: UsuarioPapelService;
  let papelService: PapelService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UsuarioPapelUpdateComponent],
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
      .overrideTemplate(UsuarioPapelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioPapelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioPapelService = TestBed.inject(UsuarioPapelService);
    papelService = TestBed.inject(PapelService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Papel query and add missing value', () => {
      const usuarioPapel: IUsuarioPapel = { id: 456 };
      const papel: IPapel = { id: 70840 };
      usuarioPapel.papel = papel;

      const papelCollection: IPapel[] = [{ id: 55647 }];
      jest.spyOn(papelService, 'query').mockReturnValue(of(new HttpResponse({ body: papelCollection })));
      const additionalPapels = [papel];
      const expectedCollection: IPapel[] = [...additionalPapels, ...papelCollection];
      jest.spyOn(papelService, 'addPapelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioPapel });
      comp.ngOnInit();

      expect(papelService.query).toHaveBeenCalled();
      expect(papelService.addPapelToCollectionIfMissing).toHaveBeenCalledWith(papelCollection, ...additionalPapels);
      expect(comp.papelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const usuarioPapel: IUsuarioPapel = { id: 456 };
      const usuario: IUsuario = { id: 71332 };
      usuarioPapel.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 68191 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioPapel });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuarioPapel: IUsuarioPapel = { id: 456 };
      const papel: IPapel = { id: 65265 };
      usuarioPapel.papel = papel;
      const usuario: IUsuario = { id: 9242 };
      usuarioPapel.usuario = usuario;

      activatedRoute.data = of({ usuarioPapel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(usuarioPapel));
      expect(comp.papelsSharedCollection).toContain(papel);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UsuarioPapel>>();
      const usuarioPapel = { id: 123 };
      jest.spyOn(usuarioPapelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioPapel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioPapelService.update).toHaveBeenCalledWith(usuarioPapel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UsuarioPapel>>();
      const usuarioPapel = new UsuarioPapel();
      jest.spyOn(usuarioPapelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioPapel }));
      saveSubject.complete();

      // THEN
      expect(usuarioPapelService.create).toHaveBeenCalledWith(usuarioPapel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UsuarioPapel>>();
      const usuarioPapel = { id: 123 };
      jest.spyOn(usuarioPapelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioPapel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioPapelService.update).toHaveBeenCalledWith(usuarioPapel);
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

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
