import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFeatures, Features } from '../features.model';
import { FeaturesService } from '../service/features.service';
import { IApp } from 'app/entities/app/app.model';
import { AppService } from 'app/entities/app/service/app.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-features-update',
  templateUrl: './features-update.component.html',
})
export class FeaturesUpdateComponent implements OnInit {
  isSaving = false;

  appsSharedCollection: IApp[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarFeatures: [null, [Validators.required]],
    nVarNome: [null, [Validators.required]],
    idnVarApp: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    app: [],
    usuario: [],
  });

  constructor(
    protected featuresService: FeaturesService,
    protected appService: AppService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ features }) => {
      this.updateForm(features);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const features = this.createFromForm();
    if (features.id !== undefined) {
      this.subscribeToSaveResponse(this.featuresService.update(features));
    } else {
      this.subscribeToSaveResponse(this.featuresService.create(features));
    }
  }

  trackAppById(_index: number, item: IApp): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeatures>>): void {
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

  protected updateForm(features: IFeatures): void {
    this.editForm.patchValue({
      id: features.id,
      idnVarFeatures: features.idnVarFeatures,
      nVarNome: features.nVarNome,
      idnVarApp: features.idnVarApp,
      idnVarUsuario: features.idnVarUsuario,
      app: features.app,
      usuario: features.usuario,
    });

    this.appsSharedCollection = this.appService.addAppToCollectionIfMissing(this.appsSharedCollection, features.app);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, features.usuario);
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

  protected createFromForm(): IFeatures {
    return {
      ...new Features(),
      id: this.editForm.get(['id'])!.value,
      idnVarFeatures: this.editForm.get(['idnVarFeatures'])!.value,
      nVarNome: this.editForm.get(['nVarNome'])!.value,
      idnVarApp: this.editForm.get(['idnVarApp'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      app: this.editForm.get(['app'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
