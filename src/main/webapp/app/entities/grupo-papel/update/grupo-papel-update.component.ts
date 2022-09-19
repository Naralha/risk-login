import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGrupoPapel, GrupoPapel } from '../grupo-papel.model';
import { GrupoPapelService } from '../service/grupo-papel.service';
import { IGrupo } from 'app/entities/grupo/grupo.model';
import { GrupoService } from 'app/entities/grupo/service/grupo.service';
import { IPapel } from 'app/entities/papel/papel.model';
import { PapelService } from 'app/entities/papel/service/papel.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-grupo-papel-update',
  templateUrl: './grupo-papel-update.component.html',
})
export class GrupoPapelUpdateComponent implements OnInit {
  isSaving = false;

  gruposSharedCollection: IGrupo[] = [];
  papelsSharedCollection: IPapel[] = [];
  empresasSharedCollection: IEmpresa[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarGrupo: [null, [Validators.required]],
    idnVarPapel: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    idnVarEmpresa: [null, [Validators.required]],
    grupo: [],
    papel: [],
    empresa: [],
    usuario: [],
  });

  constructor(
    protected grupoPapelService: GrupoPapelService,
    protected grupoService: GrupoService,
    protected papelService: PapelService,
    protected empresaService: EmpresaService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grupoPapel }) => {
      this.updateForm(grupoPapel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const grupoPapel = this.createFromForm();
    if (grupoPapel.id !== undefined) {
      this.subscribeToSaveResponse(this.grupoPapelService.update(grupoPapel));
    } else {
      this.subscribeToSaveResponse(this.grupoPapelService.create(grupoPapel));
    }
  }

  trackGrupoById(_index: number, item: IGrupo): number {
    return item.id!;
  }

  trackPapelById(_index: number, item: IPapel): number {
    return item.id!;
  }

  trackEmpresaById(_index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGrupoPapel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(grupoPapel: IGrupoPapel): void {
    this.editForm.patchValue({
      id: grupoPapel.id,
      idnVarGrupo: grupoPapel.idnVarGrupo,
      idnVarPapel: grupoPapel.idnVarPapel,
      idnVarUsuario: grupoPapel.idnVarUsuario,
      idnVarEmpresa: grupoPapel.idnVarEmpresa,
      grupo: grupoPapel.grupo,
      papel: grupoPapel.papel,
      empresa: grupoPapel.empresa,
      usuario: grupoPapel.usuario,
    });

    this.gruposSharedCollection = this.grupoService.addGrupoToCollectionIfMissing(this.gruposSharedCollection, grupoPapel.grupo);
    this.papelsSharedCollection = this.papelService.addPapelToCollectionIfMissing(this.papelsSharedCollection, grupoPapel.papel);
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, grupoPapel.empresa);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, grupoPapel.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.grupoService
      .query()
      .pipe(map((res: HttpResponse<IGrupo[]>) => res.body ?? []))
      .pipe(map((grupos: IGrupo[]) => this.grupoService.addGrupoToCollectionIfMissing(grupos, this.editForm.get('grupo')!.value)))
      .subscribe((grupos: IGrupo[]) => (this.gruposSharedCollection = grupos));

    this.papelService
      .query()
      .pipe(map((res: HttpResponse<IPapel[]>) => res.body ?? []))
      .pipe(map((papels: IPapel[]) => this.papelService.addPapelToCollectionIfMissing(papels, this.editForm.get('papel')!.value)))
      .subscribe((papels: IPapel[]) => (this.papelsSharedCollection = papels));

    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IGrupoPapel {
    return {
      ...new GrupoPapel(),
      id: this.editForm.get(['id'])!.value,
      idnVarGrupo: this.editForm.get(['idnVarGrupo'])!.value,
      idnVarPapel: this.editForm.get(['idnVarPapel'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      idnVarEmpresa: this.editForm.get(['idnVarEmpresa'])!.value,
      grupo: this.editForm.get(['grupo'])!.value,
      papel: this.editForm.get(['papel'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
