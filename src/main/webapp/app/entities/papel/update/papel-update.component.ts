import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPapel, Papel } from '../papel.model';
import { PapelService } from '../service/papel.service';
import { IApp } from 'app/entities/app/app.model';
import { AppService } from 'app/entities/app/service/app.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-papel-update',
  templateUrl: './papel-update.component.html',
})
export class PapelUpdateComponent implements OnInit {
  isSaving = false;

  appsSharedCollection: IApp[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarPapel: [null, [Validators.required]],
    nVarNome: [null, [Validators.required]],
    idnVarApp: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    app: [],
    usuario: [],
  });

  constructor(
    protected papelService: PapelService,
    protected appService: AppService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ papel }) => {
      this.updateForm(papel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const papel = this.createFromForm();
    if (papel.id !== undefined) {
      this.subscribeToSaveResponse(this.papelService.update(papel));
    } else {
      this.subscribeToSaveResponse(this.papelService.create(papel));
    }
  }

  trackAppById(_index: number, item: IApp): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPapel>>): void {
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

  protected updateForm(papel: IPapel): void {
    this.editForm.patchValue({
      id: papel.id,
      idnVarPapel: papel.idnVarPapel,
      nVarNome: papel.nVarNome,
      idnVarApp: papel.idnVarApp,
      idnVarUsuario: papel.idnVarUsuario,
      app: papel.app,
      usuario: papel.usuario,
    });

    this.appsSharedCollection = this.appService.addAppToCollectionIfMissing(this.appsSharedCollection, papel.app);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, papel.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.appService
      .query()
      .pipe(map((res: HttpResponse<IApp[]>) => res.body ?? []))
      .pipe(map((apps: IApp[]) => this.appService.addAppToCollectionIfMissing(apps, this.editForm.get('app')!.value)))
      .subscribe((apps: IApp[]) => (this.appsSharedCollection = apps));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IPapel {
    return {
      ...new Papel(),
      id: this.editForm.get(['id'])!.value,
      idnVarPapel: this.editForm.get(['idnVarPapel'])!.value,
      nVarNome: this.editForm.get(['nVarNome'])!.value,
      idnVarApp: this.editForm.get(['idnVarApp'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      app: this.editForm.get(['app'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
