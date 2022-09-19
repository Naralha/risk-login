import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPermissionsPapel, PermissionsPapel } from '../permissions-papel.model';
import { PermissionsPapelService } from '../service/permissions-papel.service';
import { IPapel } from 'app/entities/papel/papel.model';
import { PapelService } from 'app/entities/papel/service/papel.service';
import { IPermissions } from 'app/entities/permissions/permissions.model';
import { PermissionsService } from 'app/entities/permissions/service/permissions.service';
import { IFeatures } from 'app/entities/features/features.model';
import { FeaturesService } from 'app/entities/features/service/features.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-permissions-papel-update',
  templateUrl: './permissions-papel-update.component.html',
})
export class PermissionsPapelUpdateComponent implements OnInit {
  isSaving = false;

  papelsSharedCollection: IPapel[] = [];
  permissionsSharedCollection: IPermissions[] = [];
  featuresSharedCollection: IFeatures[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarPermissions: [null, [Validators.required]],
    idnVarPapel: [null, [Validators.required]],
    idnVarFeatures: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    papel: [],
    permissions: [],
    features: [],
    usuario: [],
  });

  constructor(
    protected permissionsPapelService: PermissionsPapelService,
    protected papelService: PapelService,
    protected permissionsService: PermissionsService,
    protected featuresService: FeaturesService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionsPapel }) => {
      this.updateForm(permissionsPapel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permissionsPapel = this.createFromForm();
    if (permissionsPapel.id !== undefined) {
      this.subscribeToSaveResponse(this.permissionsPapelService.update(permissionsPapel));
    } else {
      this.subscribeToSaveResponse(this.permissionsPapelService.create(permissionsPapel));
    }
  }

  trackPapelById(_index: number, item: IPapel): number {
    return item.id!;
  }

  trackPermissionsById(_index: number, item: IPermissions): number {
    return item.id!;
  }

  trackFeaturesById(_index: number, item: IFeatures): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissionsPapel>>): void {
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

  protected updateForm(permissionsPapel: IPermissionsPapel): void {
    this.editForm.patchValue({
      id: permissionsPapel.id,
      idnVarPermissions: permissionsPapel.idnVarPermissions,
      idnVarPapel: permissionsPapel.idnVarPapel,
      idnVarFeatures: permissionsPapel.idnVarFeatures,
      idnVarUsuario: permissionsPapel.idnVarUsuario,
      papel: permissionsPapel.papel,
      permissions: permissionsPapel.permissions,
      features: permissionsPapel.features,
      usuario: permissionsPapel.usuario,
    });

    this.papelsSharedCollection = this.papelService.addPapelToCollectionIfMissing(this.papelsSharedCollection, permissionsPapel.papel);
    this.permissionsSharedCollection = this.permissionsService.addPermissionsToCollectionIfMissing(
      this.permissionsSharedCollection,
      permissionsPapel.permissions
    );
    this.featuresSharedCollection = this.featuresService.addFeaturesToCollectionIfMissing(
      this.featuresSharedCollection,
      permissionsPapel.features
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      permissionsPapel.usuario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.papelService
      .query()
      .pipe(map((res: HttpResponse<IPapel[]>) => res.body ?? []))
      .pipe(map((papels: IPapel[]) => this.papelService.addPapelToCollectionIfMissing(papels, this.editForm.get('papel')!.value)))
      .subscribe((papels: IPapel[]) => (this.papelsSharedCollection = papels));

    this.permissionsService
      .query()
      .pipe(map((res: HttpResponse<IPermissions[]>) => res.body ?? []))
      .pipe(
        map((permissions: IPermissions[]) =>
          this.permissionsService.addPermissionsToCollectionIfMissing(permissions, this.editForm.get('permissions')!.value)
        )
      )
      .subscribe((permissions: IPermissions[]) => (this.permissionsSharedCollection = permissions));

    this.featuresService
      .query()
      .pipe(map((res: HttpResponse<IFeatures[]>) => res.body ?? []))
      .pipe(
        map((features: IFeatures[]) =>
          this.featuresService.addFeaturesToCollectionIfMissing(features, this.editForm.get('features')!.value)
        )
      )
      .subscribe((features: IFeatures[]) => (this.featuresSharedCollection = features));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IPermissionsPapel {
    return {
      ...new PermissionsPapel(),
      id: this.editForm.get(['id'])!.value,
      idnVarPermissions: this.editForm.get(['idnVarPermissions'])!.value,
      idnVarPapel: this.editForm.get(['idnVarPapel'])!.value,
      idnVarFeatures: this.editForm.get(['idnVarFeatures'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      papel: this.editForm.get(['papel'])!.value,
      permissions: this.editForm.get(['permissions'])!.value,
      features: this.editForm.get(['features'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
