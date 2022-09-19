import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPermissions, Permissions } from '../permissions.model';
import { PermissionsService } from '../service/permissions.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-permissions-update',
  templateUrl: './permissions-update.component.html',
})
export class PermissionsUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarPermissions: [null, [Validators.required]],
    nVarNome: [null, [Validators.required]],
    nVarTipoPermissao: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    usuario: [],
  });

  constructor(
    protected permissionsService: PermissionsService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissions }) => {
      this.updateForm(permissions);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permissions = this.createFromForm();
    if (permissions.id !== undefined) {
      this.subscribeToSaveResponse(this.permissionsService.update(permissions));
    } else {
      this.subscribeToSaveResponse(this.permissionsService.create(permissions));
    }
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissions>>): void {
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

  protected updateForm(permissions: IPermissions): void {
    this.editForm.patchValue({
      id: permissions.id,
      idnVarPermissions: permissions.idnVarPermissions,
      nVarNome: permissions.nVarNome,
      nVarTipoPermissao: permissions.nVarTipoPermissao,
      idnVarUsuario: permissions.idnVarUsuario,
      usuario: permissions.usuario,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, permissions.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IPermissions {
    return {
      ...new Permissions(),
      id: this.editForm.get(['id'])!.value,
      idnVarPermissions: this.editForm.get(['idnVarPermissions'])!.value,
      nVarNome: this.editForm.get(['nVarNome'])!.value,
      nVarTipoPermissao: this.editForm.get(['nVarTipoPermissao'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
